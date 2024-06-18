package agh.edu.pl.slpbackend.reports.kzwa;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.reports.XLSXFilesHelper;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KZWAReportGenerator extends XLSXFilesHelper {

    @NonNull
    private final ExaminationRepository examinationRepository;
    private Sample sample;
    private List<Examination> examinationList;

    public void generateReport() {
        String templateFilePath = "report_templates/kzwa_template.xlsx";
        String home = System.getProperty("user.home");
        String newFilePath = home + "/Downloads/" + "kzwa.xlsx";

        try (FileInputStream fileInputStream = new FileInputStream(templateFilePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            modifySheet(sheet, workbook);

            try (FileOutputStream fileOutputStream = new FileOutputStream(newFilePath)) {
                workbook.write(fileOutputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
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

        fillExaminationsTable(sheet, workbook);
    }

    private void fillExaminationsTable(Sheet sheet, Workbook workbook) {

        for (int i = 0; i < examinationList.size(); i++) {
            int lastRowNum = sheet.getLastRowNum();

            Examination examination = examinationList.get(i);
            Row newMainRow = sheet.createRow(lastRowNum + 1);
            newMainRow.setHeight((short) 1000);
            createCellAtIndexWithValue(newMainRow, 0, Integer.toString(i), workbook);
            createCellAtIndexWithValue(newMainRow, 1, examination.getIndication().getName() + "[g]", workbook);
            createCellAtIndexWithValue(newMainRow, 2, "max 0, 1 - specyf", workbook);
            createCellAtIndexWithValue(newMainRow, 3, "PN-A- 75101/17:1990", workbook);

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
}