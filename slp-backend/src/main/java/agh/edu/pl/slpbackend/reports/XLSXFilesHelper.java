package agh.edu.pl.slpbackend.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

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

}
