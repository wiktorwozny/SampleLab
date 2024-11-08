package agh.edu.pl.slpbackend.reports.samplereport;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.reports.FilePathResolver;
import agh.edu.pl.slpbackend.reports.XLSXFilesHelper;
import com.spire.doc.*;
import com.spire.doc.documents.PageOrientation;
import com.spire.doc.fields.TextRange;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.XmlUtils;
import org.docx4j.wml.Tbl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XLSXtoTblConverter extends XLSXFilesHelper {

    private final String tableXlsxFilePath = "table.xlsx";
    private final String modifiedTableXlsxFilePath = "modified.xlsx";
    private final String docxFilePath = "converted_docx.docx";
    private final boolean[] pattern;
    private final boolean uncertaintyExists;
    private final List<Examination> examinationList;
    private List<Examination> basicExaminationList;
    private FilePathResolver pathResolver = new FilePathResolver();

    public XLSXtoTblConverter(boolean[] pattern, boolean uncertaintyExists, List<Examination> examinationList) {
        this.pattern = pattern;
        this.uncertaintyExists = uncertaintyExists;
        this.examinationList = examinationList;

        this.basicExaminationList = examinationList.stream()
                .filter(exam -> exam.getIndication() != null && Boolean.FALSE.equals(exam.getIndication().isOrganoleptic()))
                .collect(Collectors.toList());
        this.basicExaminationList.sort(Comparator.comparing(Examination::getStartDate)
                .thenComparing(Examination::getEndDate)
                .thenComparing(exam -> exam.getIndication().getId()));
    }

    public Tbl convert() throws Exception {
        modifyTableFile();
        createDocxFileWithGeneratedTable();

        String xmlContent = null;
        try {
            xmlContent = readXmlContentFromDocx(docxFilePath, pathResolver);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tableXmlContent = extractTableXml(xmlContent);
        Object convertedTable = XmlUtils.unmarshalString(tableXmlContent);

        if (convertedTable instanceof Tbl) {
            return (Tbl) convertedTable;
        }

        return null;
    }

    private void modifyTableFile() {
        try (FileInputStream fileInputStream = new FileInputStream(pathResolver.getFullPath(tableXlsxFilePath));
             org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            modifySheet(sheet, workbook);

            try (FileOutputStream fileOutputStream = new FileOutputStream(pathResolver.getFullPath(modifiedTableXlsxFilePath))) {
                workbook.write(fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifySheet(Sheet sheet, org.apache.poi.ss.usermodel.Workbook workbook) {

        if (uncertaintyExists) {
            Cell resultCell = sheet.getRow(0).getCell(3);
            resultCell.setCellValue(resultCell.getStringCellValue() + " ± niepewność pomiaru/ [niepewność pomiaru]");
        }

        mergeRequirementsCells(sheet);
        addRowsToExaminationsTable(sheet, workbook);
    }

    private void mergeRequirementsCells(Sheet sheet) {
        long requirementsColumnsNumber = IntStream.range(0, pattern.length)
                .filter(i -> pattern[i])
                .count();

        if (requirementsColumnsNumber == 3) {
            return;
        }

        if (requirementsColumnsNumber == 2) {
            if (!pattern[0]) {
                copyCellValue(sheet.getRow(1).getCell(6), sheet.getRow(1).getCell(5));
                mergeCells(sheet, 1, 1, 5, 6);
                for (int i = 0; i < basicExaminationList.size(); i++) {
                    mergeCells(sheet, 2 + i, 2 + i, 5, 6);
                }
            } else if (!pattern[1]) {
                mergeCells(sheet, 1, 1, 5, 6);
                for (int i = 0; i < basicExaminationList.size(); i++) {
                    mergeCells(sheet, 2 + i, 2 + i, 5, 6);
                }

            } else if (!pattern[2]) {
                mergeCells(sheet, 1, 1, 6, 7);
                for (int i = 0; i < basicExaminationList.size(); i++) {
                    mergeCells(sheet, 2 + i, 2 + i, 6, 7);
                }
            }
        }

        if (requirementsColumnsNumber == 1) {
            int requirementLeft = 5;
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i]) {
                    requirementLeft += i;
                    break;
                }
            }
            copyCellValue(sheet.getRow(1).getCell(requirementLeft), sheet.getRow(1).getCell(5));
            mergeCells(sheet, 1, 1, 5, 7);
        }
    }

    private void addRowsToExaminationsTable(Sheet sheet, org.apache.poi.ss.usermodel.Workbook workbook) {
        for (int i = 0; i < basicExaminationList.size(); i++) {
            Examination examination = basicExaminationList.get(i);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            createCellAtIndexWithValue(newRow, 0, String.valueOf(i + 1), workbook);
            createCellAtIndexWithValue(newRow, 1, examination.getIndication().getName(), workbook);
            createCellAtIndexWithValue(newRow, 2, examination.getIndication().getMethod(), workbook);
            createCellAtIndexWithValue(newRow, 3, uncertaintyExists ? "16,7 ± " + examination.getUncertainty() : "16,7", workbook);
            createCellAtIndexWithValue(newRow, 4, examination.getIndication().getUnit(), workbook);
            int colNum = 5;

            if (pattern[0]) {
                createCellAtIndexWithValue(newRow, colNum, !examination.getSignage().equals("") ? examination.getSignage() : "-", workbook);
                colNum += 1;
            }

            if (pattern[1]) {
                createCellAtIndexWithValue(newRow, colNum, !examination.getSpecification().equals("") ? examination.getSpecification() : "-", workbook);
                colNum += 1;
            }

            if (pattern[2]) {
                if ((!pattern[0] && pattern[1]) || (!pattern[1] && pattern[0])) {
                    colNum = colNum + 1;
                }
                createCellAtIndexWithValue(newRow, colNum, !examination.getNutritionalValue().equals("") ? examination.getNutritionalValue() : "-", workbook);
            }
        }
    }

    private void createDocxFileWithGeneratedTable() {
        Workbook workbook = new Workbook();
        workbook.loadFromFile(pathResolver.getFullPath(modifiedTableXlsxFilePath));

        Worksheet sheet = workbook.getWorksheets().get(0);

        Document doc = new Document();
        Section section = doc.addSection();
        section.getPageSetup().setOrientation(PageOrientation.Portrait);

        Table table = section.addTable(true);
        table.resetCells(sheet.getLastRow(), sheet.getLastColumn());

        mergeCellsInDocxFile(sheet, table);

        for (int r = 1; r <= sheet.getLastRow(); r++) {

            table.getRows().get(r - 1).setHeight((float) sheet.getRowHeight(r));

            for (int c = 1; c <= sheet.getLastColumn(); c++) {
                CellRange xCell = sheet.getCellRange(r, c);
                TableCell wCell = table.get(r - 1, c - 1);

                TextRange textRange = wCell.addParagraph().appendText(xCell.getValue());

                copyStyle(textRange, xCell, wCell);
            }
        }

        doc.saveToFile(pathResolver.getFullPath(docxFilePath), FileFormat.Docx);
    }

    public void cleanFiles() throws Exception {
        Files.delete(Paths.get(pathResolver.getFullPath(modifiedTableXlsxFilePath)));
        Files.delete(Paths.get(pathResolver.getFullPath(docxFilePath)));
    }
}
