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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.XmlUtils;
import org.docx4j.wml.Tbl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class XLSXorganolepticToTblConverter extends XLSXFilesHelper {

    private final String tableXlsxFilePath = "organoleptic_table.xlsx";
    private final String modifiedTableXlsxFilePath = "organoleptic_modified.xlsx";
    private final String docxFilePath = "organoleptic_converted_docx.docx";
    private final List<Examination> examinationList;
    private final String organolepticMethod;
    private final List<Examination> organolepticExaminationList;
    private final FilePathResolver pathResolver = new FilePathResolver();

    public XLSXorganolepticToTblConverter(List<Examination> examinationList, String organolepticMethod) {
        this.examinationList = examinationList;
        this.organolepticMethod = organolepticMethod;
        this.organolepticExaminationList = examinationList.stream()
                .filter(exam -> exam.getIndication() != null && Boolean.TRUE.equals(exam.getIndication().isOrganoleptic()))
                .collect(Collectors.toList());
    }

    public Tbl convert() throws Exception {
        if (this.organolepticExaminationList.isEmpty()) {
            return null;
        }

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
        addRowsToTable(sheet, workbook);
        prepareOrganolepticCells(sheet, workbook);
    }

    private void addRowsToTable(Sheet sheet, org.apache.poi.ss.usermodel.Workbook workbook) {
        for (int i = 0; i < organolepticExaminationList.size(); i++) {
            Examination examination = organolepticExaminationList.get(i);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            createCellAtIndexWithValue(newRow, 0, examination.getIndication().getName(), workbook);
            createCellAtIndexWithValue(newRow, 1, i == 0 ? this.organolepticMethod : "", workbook);
            createCellAtIndexWithValue(newRow, 2, examination.getResult(), workbook);
            createCellAtIndexWithValue(newRow, 3, examination.getSignage(), workbook);
        }
    }

    private void prepareOrganolepticCells(Sheet sheet, org.apache.poi.ss.usermodel.Workbook workbook) {
        String titleCellValue = sheet.getRow(0).getCell(0).getStringCellValue();
        int lp = examinationList.size() - organolepticExaminationList.size() + 1;
        sheet.getRow(0).getCell(0).setCellValue(lp + ". " + titleCellValue);

        if (organolepticExaminationList.size() > 1) {
            mergeCells(sheet, 2, organolepticExaminationList.size() + 1, 1, 1);
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
