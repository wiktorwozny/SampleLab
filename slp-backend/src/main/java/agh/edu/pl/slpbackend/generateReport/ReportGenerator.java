package agh.edu.pl.slpbackend.generateReport;


import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ReportGenerator {

    private final ExaminationRepository examinationRepository;

    public void generateReport(final Sample sample) throws Exception {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                    .load(new java.io.File("report_templates/main_report_template.docx"));

            Map<String, String> fieldsMap = getStringStringHashMap(sample);

            HeaderPart headerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultHeader();
            FooterPart footerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultFooter();
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            List<Object> tables = documentPart.getJAXBNodesViaXPath("//w:tbl", true);

            Tbl examinationsTimesTable = findNthTable(tables, 2);

            if (examinationsTimesTable != null) {
                fillExaminationsTimesTable(examinationsTimesTable, sample);
            }

            documentPart.variableReplace(fieldsMap);
            headerPart.variableReplace(fieldsMap);
            footerPart.variableReplace(fieldsMap);

            String home = System.getProperty("user.home");
            Docx4J.save(wordMLPackage, new File(home + "/Downloads/" + "report.docx"));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private Map<String, String> getStringStringHashMap(final Sample sample) {
        Map<String, String> fieldsMap = new HashMap<>();
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

    private Tbl findNthTable(List<Object> tables, int n) {

        if (n < tables.size()) {
            return (Tbl) ((JAXBElement<?>) tables.get(n)).getValue();
        }

        return null;
    }

    private void fillExaminationsTimesTable(Tbl examinationsTimesTable, Sample sample) {
        List<Examination> examinationList = examinationRepository.findBySampleId(sample.getId());

        for (Examination examination: examinationList) {
            LocalDate startDate = examination.getStartDate();
            LocalDate endDate = examination.getEndDate();

            if (startDate == null && endDate == null) {
                continue;
            }

            String lp = examination.getIndication().getId().toString();

            addRowToTable(examinationsTimesTable, formatLocalDate(startDate), lp, formatLocalDate(endDate), lp);
        }
    }

    private void addRowToTable(Tbl table, String... cellTexts) {
        Tr row = Context.getWmlObjectFactory().createTr();

        for (String cellText : cellTexts) {
            Tc cell = Context.getWmlObjectFactory().createTc();
            P paragraph = Context.getWmlObjectFactory().createP();
            R run = Context.getWmlObjectFactory().createR();
            Text text = Context.getWmlObjectFactory().createText();
            text.setValue(cellText);

            run.getContent().add(text);
            paragraph.getContent().add(run);
            cell.getContent().add(paragraph);

            TcPr tcPr = Context.getWmlObjectFactory().createTcPr();
            TcPrInner.TcBorders borders = Context.getWmlObjectFactory().createTcPrInnerTcBorders();
            borders.setTop(createBorder());
            borders.setBottom(createBorder());
            borders.setLeft(createBorder());
            borders.setRight(createBorder());
            tcPr.setTcBorders(borders);
            cell.setTcPr(tcPr);

            row.getContent().add(cell);
        }

        table.getContent().add(row);
    }

    private static CTBorder createBorder() {
        CTBorder border = Context.getWmlObjectFactory().createCTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger("4"));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);
        return border;
    }

    private String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    private String formatLocalDate(LocalDate date) {
        if (date == null) {
            return "-";
        }

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(pattern);
    }

    private String formatAddress(Address address) {
        return "ul. " + address.getStreet() + "\n" + address.getZipCode() + ", " + address.getCity();
    }

}
