package agh.edu.pl.slpbackend.generateReport;


import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Sample;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class GenerateReport {

    public void generateReport(final Sample sample) throws Exception {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                    .load(new java.io.File("D:\\Studia_AGH\\praca inżynierska\\SampleLab\\slp-backend\\report_templates\\test.docx"));

            HashMap<String, String> fieldsMap = getStringStringHashMap(sample);

            HeaderPart headerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultHeader();
            FooterPart footerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultFooter();
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            documentPart.variableReplace(fieldsMap);
            System.out.println(fieldsMap);
            headerPart.variableReplace(fieldsMap);
            footerPart.variableReplace(fieldsMap);

            String home = System.getProperty("user.home");
            Docx4J.save(wordMLPackage, new File(home + "/Downloads/" + "report.docx"));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private HashMap<String, String> getStringStringHashMap(final Sample sample) {
        HashMap<String, String> fieldsMap = new HashMap<String, String>();
        fieldsMap.put("labName", "Moje Laboratorium");
        fieldsMap.put("city", "Moje Miasto");
        fieldsMap.put("fullAddress", "Mój pełny adres 96 pod muchomorem");
        fieldsMap.put("phoneNumber", "999 998 997");
        fieldsMap.put("fax", "co to fax? XD");
        fieldsMap.put("email", "laboratoryemail@email.com");
        fieldsMap.put("sampleId", "jekieś IDK XDD");
        fieldsMap.put("newDate", getCurrentTime());
        fieldsMap.put("counter", Integer.toString(1));
        fieldsMap.put("country", "POLSKA GUROM");

        String supplierOrSellerName = sample.getReportData().getSupplierName() != null
                ? sample.getReportData().getSupplierName()
                : sample.getReportData().getSellerName();

        fieldsMap.put("supplierName", supplierOrSellerName);
        fieldsMap.put("recipientName", sample.getReportData().getRecipientName());
        fieldsMap.put("jobNumber", String.valueOf(sample.getReportData().getJobNumber()));
        fieldsMap.put("samplingStandard", sample.getSamplingStandard().getName());
        fieldsMap.put("samplingDate", getCurrentTime());
        fieldsMap.put("mechanism", sample.getReportData().getMechanism());
        fieldsMap.put("batchSize", "[rozmiar partii]");
        fieldsMap.put("batchNumber", "[numer partii]");
        fieldsMap.put("productionDate", getCurrentTime());
        fieldsMap.put("expirationDate", formatLocalDate(sample.getExpirationDate()));
        fieldsMap.put("clientName", sample.getClient().getName());
        fieldsMap.put("clientAddress", formatAddress(sample.getClient().getAddress()));
        fieldsMap.put("admissionDate", formatLocalDate(sample.getAdmissionDate()));
        fieldsMap.put("sampleDesc", "Lorem ipsum");
        fieldsMap.put("sampleSize", sample.getSize());
        fieldsMap.put("sampleState", sample.getState());


        return fieldsMap;
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
