package agh.edu.pl.slpbackend.reports.kzwa;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.reports.FilePathResolver;
import agh.edu.pl.slpbackend.reports.XLSXFilesHelper;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KZWAReportGenerator extends XLSXFilesHelper {

    @NonNull
    private final ExaminationRepository examinationRepository;
    private Sample sample;
    private List<Examination> examinationList;
    private FilePathResolver pathResolver = new FilePathResolver();

    public ByteArrayOutputStream generateReport() {
        String templateFilePath = "kzwa_template.xlsx";

        try (FileInputStream fileInputStream = new FileInputStream(pathResolver.getFullPath(templateFilePath));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet templateSheet = workbook.getSheetAt(0);

            Map<String, List<Examination>> examinationsByLab = examinationList.stream()
                    .collect(Collectors.groupingBy(exam -> exam.getIndication().getLaboratory()));

            for (Map.Entry<String, List<Examination>> entry : examinationsByLab.entrySet()) {
                String labName = entry.getKey();
                List<Examination> labExaminations = entry.getValue();

                Sheet labSheet = workbook.createSheet(labName);
                copySheet(templateSheet, labSheet);

                modifySheet(labSheet, workbook);

                fillExaminationsTable(labSheet, workbook, labExaminations);
            }

            workbook.removeSheetAt(0);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void modifySheet(Sheet sheet, Workbook workbook) {
        Map<String, String> specificTextsMap = new HashMap<>();
        specificTextsMap.put("Nazwa artykułu rolno-spożywczego:", " nazwa jakas");
        specificTextsMap.put("Wielkość próbki:", " " + sample.getSize());

        for (String specificText: specificTextsMap.keySet()) {
            changeCellWithSpecificText(sheet, specificText, specificTextsMap.get(specificText));
        }

        Map<String, String> cellReferenceMap = new HashMap<>();
        cellReferenceMap.put("C3", "jakies laboratorium");
        cellReferenceMap.put("I5", getCurrentTime());

        for (String cellReference: cellReferenceMap.keySet()) {
            changeCellWithCellReference(sheet, cellReference, cellReferenceMap.get(cellReference));
        }
    }

    private void fillExaminationsTable(Sheet sheet, Workbook workbook, List<Examination> labExaminations) {

        for (int i = 0; i < labExaminations.size(); i++) {
            int lastRowNum = sheet.getLastRowNum();

            Examination examination = labExaminations.get(i);
            Row newMainRow = sheet.createRow(lastRowNum + 1);
            newMainRow.setHeight((short) 1000);
            createCellAtIndexWithValue(newMainRow, 0, Integer.toString(i), workbook);
            createCellAtIndexWithValue(newMainRow, 1, examination.getIndication().getName() + "[g]", workbook);
            createCellAtIndexWithValue(newMainRow, 2, "max 0, 1 - specyf", workbook);

            String method = examination.getIndication().isOrganoleptic() ? sample.getAssortment().getOrganolepticMethod() : examination.getIndication().getMethod();
            createCellAtIndexWithValue(newMainRow, 3, method, workbook);

            for (int j = 4; j < 9; j++) {
                createCellAtIndexWithValue(newMainRow, j, "", workbook);
            }

            for (int j = 0; j < examination.getSamplesNumber(); j++) {
                Row newSubRow = sheet.createRow(lastRowNum + 2 + j);
                newSubRow.setHeight((short) 400);
                createCellAtIndexWithValue(newSubRow, 0, "", workbook);
                createCellAtIndexWithValue(newSubRow, 1, "opakowanie nr " + (j + 1), workbook);
                for (int k = 2; k < 9; k++) {
                    createCellAtIndexWithValue(newSubRow, k, "", workbook);
                }
            }

            // merging cells
            mergeCells(sheet, lastRowNum + 1, lastRowNum + 1 + examination.getSamplesNumber(), 2, 2);
            mergeCells(sheet, lastRowNum + 1, lastRowNum + 1 + examination.getSamplesNumber(), 3, 3);
        }
    }

    private String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public void setParameters(Sample sample) {
        this.sample = sample;
        this.examinationList = examinationRepository.findBySampleId(sample.getId());
    }

    private void copySheet(Sheet templateSheet, Sheet newSheet) {
        for (int i = 0; i <= templateSheet.getLastRowNum(); i++) {
            Row templateRow = templateSheet.getRow(i);
            Row newRow = newSheet.createRow(i);

            if (templateRow != null) {
                newRow.setHeight(templateRow.getHeight());
                for (int j = 0; j < templateRow.getLastCellNum(); j++) {
                    Cell templateCell = templateRow.getCell(j);
                    Cell newCell = newRow.createCell(j);

                    if (templateCell != null) {
                        newCell.setCellStyle(templateCell.getCellStyle());
                        switch (templateCell.getCellType()) {
                            case STRING:
                                newCell.setCellValue(templateCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                newCell.setCellValue(templateCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                newCell.setCellValue(templateCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                newCell.setCellFormula(templateCell.getCellFormula());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < templateSheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = templateSheet.getMergedRegion(i);
            newSheet.addMergedRegion(mergedRegion);
        }
    }
}
