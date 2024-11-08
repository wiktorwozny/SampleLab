package agh.edu.pl.slpbackend.reports;

import com.spire.doc.Table;
import com.spire.doc.TableCell;
import com.spire.doc.fields.TextRange;
import com.spire.xls.Worksheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class XLSXFilesHelper {

    public void createCellAtIndexWithValue(Row row, int i, String value, Workbook workbook) {
        Cell newCell = row.createCell(i);
        newCell.setCellValue(value);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setWrapText(true);

        Font font = workbook.createFont(); // font is always 9 TimesNewRoman, if needed I can make it parametrized
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 9);
        cellStyle.setFont(font);

        newCell.setCellStyle(cellStyle);
    }

    public void changeCellWithSpecificText(Sheet sheet, String text, String modifiedText) {
        boolean found = false;
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(text)) {
                    cell.setCellValue(text + modifiedText);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
    }

    public void changeCellWithCellReference(Sheet sheet, String reference, String modifiedText) {
        CellReference cellRef = new CellReference(reference);

        Row row = sheet.getRow(cellRef.getRow());
        if (row == null) {
            row = sheet.createRow(cellRef.getRow());
        }
        Cell cell = row.getCell(cellRef.getCol());
        if (cell == null) {
            cell = row.createCell(cellRef.getCol());
        }

        cell.setCellValue(modifiedText);
    }

    public void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress cellAddresses = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(cellAddresses);
    }

    public static void copyCellValue(Cell sourceCell, Cell destCell) {
        switch (sourceCell.getCellType()) {
            case STRING -> destCell.setCellValue(sourceCell.getStringCellValue());
            case NUMERIC -> destCell.setCellValue(sourceCell.getNumericCellValue());
            case BOOLEAN -> destCell.setCellValue(sourceCell.getBooleanCellValue());
            case FORMULA -> destCell.setCellFormula(sourceCell.getCellFormula());
            case BLANK -> destCell.setBlank();
            case ERROR -> destCell.setCellErrorValue(sourceCell.getErrorCellValue());
            default -> System.out.println("Unsupported cell type");
        }
        CellStyle newCellStyle = destCell.getSheet().getWorkbook().createCellStyle();
        newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
        destCell.setCellStyle(newCellStyle);
    }

    public String readXmlContentFromDocx(String docxFilePath, FilePathResolver pathResolver) throws IOException {
        try (ZipFile zipFile = new ZipFile(pathResolver.getFullPath(docxFilePath));
             InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("word/document.xml"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    public String extractTableXml(String xmlContent) {
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

    public void mergeCellsInDocxFile(Worksheet sheet, Table table) {
        if (sheet.hasMergedCells()) {

            com.spire.xls.CellRange[] ranges = sheet.getMergedCells();
            for (com.spire.xls.CellRange range : ranges) {
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

    public void copyStyle(TextRange wTextRange, com.spire.xls.CellRange xCell, TableCell wCell) {

        wTextRange.getCharacterFormat().setTextColor(xCell.getStyle().getFont().getColor());
        wTextRange.getCharacterFormat().setFontSize((float) xCell.getStyle().getFont().getSize());
        wTextRange.getCharacterFormat().setFontName(xCell.getStyle().getFont().getFontName());
        wTextRange.getCharacterFormat().setBold(xCell.getStyle().getFont().isBold());
        wTextRange.getCharacterFormat().setItalic(xCell.getStyle().getFont().isItalic());

        wCell.getCellFormat().setBackColor(xCell.getStyle().getColor());

        switch (xCell.getHorizontalAlignment()) {
            case Left -> wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(com.spire.doc.documents.HorizontalAlignment.Left);
            case Center ->
                    wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(com.spire.doc.documents.HorizontalAlignment.Center);
            case Right -> wTextRange.getOwnerParagraph().getFormat().setHorizontalAlignment(com.spire.doc.documents.HorizontalAlignment.Right);
        }

        switch (xCell.getVerticalAlignment()) {
            case Bottom -> wCell.getCellFormat().setVerticalAlignment(com.spire.doc.documents.VerticalAlignment.Bottom);
            case Center -> wCell.getCellFormat().setVerticalAlignment(com.spire.doc.documents.VerticalAlignment.Middle);
            case Top -> wCell.getCellFormat().setVerticalAlignment(com.spire.doc.documents.VerticalAlignment.Top);
        }
    }
}
