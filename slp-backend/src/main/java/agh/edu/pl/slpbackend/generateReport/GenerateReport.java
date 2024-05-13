package agh.edu.pl.slpbackend.generateReport;


import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Sample;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class GenerateReport {
    private HashMap<String, String> fieldsMap;

    public void generateReport(final Sample sample) throws Exception {
        try (FileInputStream filePath = new FileInputStream("report_templates/test.docx")) {
            fieldsMap = getStringStringHashMap(sample);
            XWPFDocument documentFile = new XWPFDocument(filePath);
            XWPFHeaderFooterPolicy headerFooterPolicy = documentFile.getHeaderFooterPolicy();

            changeHeader(headerFooterPolicy);
            changeFooter(headerFooterPolicy);
            changeTables(documentFile);

            FileOutputStream fos = new FileOutputStream("report_output/report.docx");
            documentFile.write(fos);
            fos.close();
            documentFile.close();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private HashMap<String, String> getStringStringHashMap(final Sample sample) {
        HashMap<String, String> fieldsMap = new HashMap<String, String>();
        fieldsMap.put("${labName}", "Moje Laboratorium");
        fieldsMap.put("${city}", "Moje Miasto");
        fieldsMap.put("${fullAddress}", "Mój pełny adres 96 pod muchomorem");
        fieldsMap.put("${phoneNumber}", "999 998 997");
        fieldsMap.put("${fax}", "co to fax? XD");
        fieldsMap.put("${email}", "laboratoryemail@email.com");
        fieldsMap.put("${sampleId}", "jekieś IDK XDD");
        fieldsMap.put("${newDate}", getCurrentTime());
        fieldsMap.put("${counter}", Integer.toString(1));
        fieldsMap.put("${country}", "POLSKA GUROM");
        fieldsMap.put("${supplierName}", sample.getReportData().getSupplierName());
        fieldsMap.put("${recipientName}", sample.getReportData().getRecipientName());
        fieldsMap.put("${jobNumber}", String.valueOf(sample.getReportData().getJobNumber()));
        fieldsMap.put("${samplingStandard}", sample.getSamplingStandard().getName());
        fieldsMap.put("${samplingDate}", getCurrentTime());
        fieldsMap.put("${mechanism}", sample.getReportData().getMechanism());
        fieldsMap.put("${batchSize}", "[rozmiar partii]");
        fieldsMap.put("${batchNumber}", "[numer partii]");
        fieldsMap.put("${productionDate}", getCurrentTime());
        fieldsMap.put("${expirationDate}", formatLocalDate(sample.getExpirationDate()));
        fieldsMap.put("${clientName}", sample.getClient().getName());
        fieldsMap.put("${clientAddress}", formatAddress(sample.getClient().getAddress()));
        fieldsMap.put("${admissionDate}", formatLocalDate(sample.getAdmissionDate()));
        fieldsMap.put("${sampleDesc}", "Lorem ipsum");
        fieldsMap.put("${sampleSize}", sample.getSize());
        fieldsMap.put("${sampleState}", sample.getState());


        return fieldsMap;
    }

    private void changeTable(XWPFTable table, HashMap<String, String> fieldsMap) {

        int rows = table.getNumberOfRows();
        for (int i = 0; i < rows; i++) {
            XWPFTableRow row = table.getRow(i);
            List<XWPFTableCell> cellList = row.getTableCells();
            for (XWPFTableCell cell : cellList) {
                String text = cell.getText();
                System.out.println(text);
                if (text != null) {
                    String[] wordsList = text.split("\\s+");
                    for (String word : wordsList) {
                        String toReplace = fieldsMap.get(word);
                        if (toReplace != null) {
                            text = text.replace(word, toReplace);
                            cell.setText(text);
                        }
                    }
                }
            }
        }
    }

    private void changeRuns(List<XWPFParagraph> paragraphList, HashMap<String, String> fieldsMap) {
        for (XWPFParagraph paragraph : paragraphList) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                System.out.println(text);
                if (text != null) {
                    String[] wordsList = text.split("\\s+");
                    for (String word : wordsList) {
                        String toReplace = fieldsMap.get(word);
                        if (toReplace != null) {
                            text = text.replace(word, toReplace);
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }

    private void changeHeader(XWPFHeaderFooterPolicy headerFooterPolicy) {

        XWPFHeader header = headerFooterPolicy.getDefaultHeader();
        List<XWPFParagraph> headerParagraphList = header.getListParagraph();
        changeRuns(headerParagraphList, fieldsMap);
    }

    private void changeFooter(XWPFHeaderFooterPolicy headerFooterPolicy) {
        XWPFFooter footer = headerFooterPolicy.getDefaultFooter();
        List<XWPFParagraph> footerParagraphList = footer.getListParagraph();
        changeRuns(footerParagraphList, fieldsMap);

        XWPFTable footerTable = footer.getTableArray(0);
        changeTable(footerTable, fieldsMap);
    }

    private void changeTables(XWPFDocument documentFile) {
        List<XWPFTable> tableList = documentFile.getTables();
        for (XWPFTable table : tableList) {
            changeTable(table, fieldsMap);
        }
    }


    private String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    private String formatLocalDate(LocalDate date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(pattern);
    }

    private String formatAddress(Address address) {
        return "ul. " + address.getStreet() + "\n" + address.getZipCode() + ", " + address.getCity();
    }

}
