package agh.edu.pl.slpbackend.reports.samplereport;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.reports.XLSXFilesHelper;
import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.PageOrientation;
import com.spire.doc.documents.VerticalAlignment;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipFile;

public class XLSXtoTblConverter extends XLSXFilesHelper {

    private final String tableXlsxFilePath = "report_templates/table.xlsx";
    private final String modifiedTableXlsxFilePath = "report_templates/modified.xlsx";
    private final String docxFilePath = "report_templates/converted_docx.docx";
    private final boolean[] pattern;
    private final boolean uncertaintyExists;
    private final List<Examination> examinationList;

    public XLSXtoTblConverter(boolean[] pattern, boolean uncertaintyExists, List<Examination> examinationList) {
        this.pattern = pattern;
        this.uncertaintyExists = uncertaintyExists;
        this.examinationList = examinationList;
    }

    public Tbl convert() throws Exception {
        modifyTableFile();
        createDocxFileWithGeneratedTable();

        String xmlContent = null;
        try {
            xmlContent = readXmlContentFromDocx(docxFilePath);
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
        try (FileInputStream fileInputStream = new FileInputStream(tableXlsxFilePath);
             org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            modifySheet(sheet, workbook);

            try (FileOutputStream fileOutputStream = new FileOutputStream(modifiedTableXlsxFilePath)) {
                workbook.write(fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifySheet(Sheet sheet, org.apache.poi.ss.usermodel.Workbook workbook) {

        if (uncertaintyExists) {
            Cell resultCell = sheet.getRow(1).getCell(3);
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
                for (int i = 0; i < examinationList.size(); i++) {
                    mergeCells(sheet, 2 + i, 2 + i, 5, 6);
                }
            } else if (!pattern[1]) {
                mergeCells(sheet, 1, 1, 5, 6);
                for (int i = 0; i < examinationList.size(); i++) {
                    mergeCells(sheet, 2 + i, 2 + i, 5, 6);
                }

            } else if (!pattern[2]) {
                mergeCells(sheet, 1, 1, 6, 7);
                for (int i = 0; i < examinationList.size(); i++) {
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
        for (int i = 0; i < examinationList.size(); i++) {
            Examination examination = examinationList.get(i);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            createCellAtIndexWithValue(newRow, 0, String.valueOf(i + 1), workbook);
            createCellAtIndexWithValue(newRow, 1, examination.getIndication().getName(), workbook);
            createCellAtIndexWithValue(newRow, 2, "PN-NE ISO 123:123", workbook);
            createCellAtIndexWithValue(newRow, 3, uncertaintyExists ? "16,7 " + examination.getUncertainty() : "16,7", workbook);
            createCellAtIndexWithValue(newRow, 4, "[giet]", workbook);
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

    private String readXmlContentFromDocx(String docxFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(docxFilePath);
             InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("word/document.xml"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    private String extractTableXml(String xmlContent) {
        int startIndex = xmlContent.indexOf("<w:tbl");

        if (startIndex != -1) {
            int endIndex = xmlContent.indexOf("</w:tbl>", startIndex);

            if (endIndex != -1) {
                endIndex += "</w:tbl>".length();
                String tableXml = xmlContent.substring(startIndex, endIndex);

                // I have to ensure that the namespace declaration is included, this spire library pretty bad xd
                if (!tableXml.contains("xmlns:w=")) {
                    tableXml = tableXml.replaceFirst("<w:tbl", "<w:tbl xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"");
                }

                return tableXml;
            }
        }

        return null;
    }

    private void createDocxFileWithGeneratedTable() {
        Workbook workbook = new Workbook();
        workbook.loadFromFile(modifiedTableXlsxFilePath);

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

        doc.saveToFile(docxFilePath, FileFormat.Docx);
    }

    private void mergeCellsInDocxFile(Worksheet sheet, Table table) {
        if (sheet.hasMergedCells()) {

            CellRange[] ranges = sheet.getMergedCells();
            for (CellRange range : ranges) {
                int startRow = range.getRow();
                int startColumn = range.getColumn();
                int rowCount = range.getRowCount();
                int columnCount = range.getColumnCount();

                if (rowCount > 1 && columnCount > 1) {
                    for (int j = startRow; j <= startRow + rowCount; j++) {
                        table.applyHorizontalMerge(j - 1, startColumn - 1, startColumn - 1 + columnCount - 1);
                    }
                    table.applyVerticalMerge(startColumn - 1, startRow - 1, startRow - 1 + rowCount - 1);
                }
                if (rowCount > 1 && columnCount == 1) {
                    table.applyVerticalMerge(startColumn - 1, startRow - 1, startRow - 1 + rowCount - 1);
                }
                if (columnCount > 1 && rowCount == 1) {
                    table.applyHorizontalMerge(startRow - 1, startColumn - 1, startColumn - 1 + columnCount - 1);
                }
            }
        }
    }

    private void copyStyle(TextRange wTextRange, CellRange xCell, TableCell wCell) {

        wTextRange.getCharacterFormat().setTextColor(xCell.getStyle().getFont().getColor());
        wTextRange.getCharacterFormat().setFontSize((float) xCell.getStyle().getFont().getSize());
        wTextRange.getCharacterFormat().setFontName(xCell.getStyle().getFont().getFontName());
        wTextRange.getCharacterFormat().setBold(xCell.getStyle().getFont().isBold());
        wTextRange.getCharacterFormat().setItalic(xCell.getStyle().getFont().isItalic());

        wCell.getCellFormat().setBackColor(xCell.getStyle().getColor());

        switch (xCell.getHorizontalAlignment()) {
            case Left -> wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Left);
            case Center ->
                    wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
            case Right -> wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(HorizontalAlignment.Right);
        }

        switch (xCell.getVerticalAlignment()) {
            case Bottom -> wCell.getCellFormat().setVerticalAlignment(VerticalAlignment.Bottom);
            case Center -> wCell.getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
            case Top -> wCell.getCellFormat().setVerticalAlignment(VerticalAlignment.Top);
        }
    }

    public void cleanFiles() throws Exception {
        Files.delete(Paths.get(modifiedTableXlsxFilePath));
        Files.delete(Paths.get(docxFilePath));
    }
}
