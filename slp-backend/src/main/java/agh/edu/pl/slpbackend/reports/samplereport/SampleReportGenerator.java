package agh.edu.pl.slpbackend.reports.samplereport;


import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleReportGenerator {

    @NonNull
    private final ExaminationRepository examinationRepository;
    private Sample sample;
    private List<Examination> examinationList;

    public void generateReport() throws Exception {
        if (sample == null) {
            throw new IllegalStateException("Sample not set");
        }

        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                    .load(new java.io.File("report_templates/sample_report_template.docx"));

            boolean uncertaintyExists = checkIfUncertaintyExists();

            Map<String, String> fieldsMap = getStringStringHashMap(uncertaintyExists);

            HeaderPart headerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultHeader();
            FooterPart footerPart = wordMLPackage.getHeaderFooterPolicy().getDefaultFooter();
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            List<Object> tables = documentPart.getJAXBNodesViaXPath("//w:tbl", true);

            Tbl examinationsTimesTable = findNthTable(tables, 2);

            if (examinationsTimesTable != null) {
                fillExaminationsTimesTable(examinationsTimesTable);
            }

            documentPart.variableReplace(fieldsMap);
            headerPart.variableReplace(fieldsMap);
            footerPart.variableReplace(fieldsMap);

            boolean[] pattern = getRequirementsPattern();
            XLSXtoTblConverter xlsXtoTblConverter = new XLSXtoTblConverter(pattern, uncertaintyExists, examinationList);
            Tbl convertedExaminationsTable = xlsXtoTblConverter.convert();

            adjustExaminationsTableSize(convertedExaminationsTable, pattern);
            addTableAtParagraph(documentPart, "EXAMINATIONS_TABLE", convertedExaminationsTable);

            String home = System.getProperty("user.home");
            Docx4J.save(wordMLPackage, new File(home + "/Downloads/" + "report.docx"));

            xlsXtoTblConverter.cleanFiles();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private Map<String, String> getStringStringHashMap(boolean uncertaintyExists) {
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

        // uncertainty cases
        if (uncertaintyExists) {
            fieldsMap.put("uncertaintyInfo", """
                    Podana niepewność jest niepewnością rozszerzoną, uzyskaną przez pomnożenie niepewności standardowej\s
                    i współczynnika rozszerzenia k=2, co w przybliżeniu zapewnia poziom ufności 95%.
                    Podana niepewność pomiaru oszacowana została tylko i wyłącznie dla podanej metodyki badawczej.
                    """);
        } else {
            fieldsMap.put("uncertaintyInfo", "");
        }


        return fieldsMap;
    }

    private Tbl findNthTable(List<Object> tables, int n) {

        if (n < tables.size()) {
            return (Tbl) ((JAXBElement<?>) tables.get(n)).getValue();
        }

        return null;
    }

    private void fillExaminationsTimesTable(Tbl examinationsTimesTable) {
        if (examinationList == null) {
            throw new IllegalStateException("ExaminationList not set");
        }

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
            if (cellText == null) {
                continue;
            }

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

    private void addTableAtParagraph(MainDocumentPart documentPart, String placeholder, Tbl table) {
        List<Object> content = documentPart.getContent();

        for (Object object : documentPart.getContent()) {
            if (object instanceof P) {
                P paragraph = (P) object;
                String paragraphText = getParagraphText(paragraph);
                if (placeholder.equals(paragraphText.trim())) {
                    int index = content.indexOf(paragraph);
                    content.set(index, table);
                }
            }
        }
    }

    private String getParagraphText(P paragraph) {
        StringBuilder text = new StringBuilder();
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                for (Object o : run.getContent()) {
                    if (o instanceof JAXBElement) {
                        JAXBElement jaxbElement = (JAXBElement) o;
                        if (jaxbElement.getValue() instanceof Text) {
                            text.append(((Text) jaxbElement.getValue()).getValue());
                        }
                    }
                }
            }
        }
        return text.toString();
    }

    private void adjustExaminationsTableSize(Tbl table, boolean[] pattern) {

        List<Object> rowObjects = table.getContent();
        int totalWidth = getTableTotalWidthFromRow((Tr) rowObjects.get(0));

        Tc requirementsTitleCell = (Tc) ((JAXBElement) (((Tr) rowObjects.get(0)).getContent().get(5))).getValue();
        int requirementsTitleCellWidth = requirementsTitleCell.getTcPr().getTcW().getW().intValue();
        int mainColumnsTotalWidth = totalWidth - requirementsTitleCellWidth;

        List<Integer> widthsToSet = new ArrayList<>();

        widthsToSet.add(mainColumnsTotalWidth * 1 / 19);
        widthsToSet.add(mainColumnsTotalWidth * 6 / 19);
        widthsToSet.add(mainColumnsTotalWidth * 5 / 19);
        widthsToSet.add(mainColumnsTotalWidth * 4 / 19);
        widthsToSet.add(mainColumnsTotalWidth * 3 / 19);

        // first row
        Tr firstRow = (Tr) rowObjects.get(0);
        for (int i = 0; i < widthsToSet.size(); i++) {
            Tc cell = (Tc) ((JAXBElement) firstRow.getContent().get(i)).getValue();
            TblWidth tblWidth = new TblWidth();
            tblWidth.setW(BigInteger.valueOf(widthsToSet.get(i)));
            cell.getTcPr().setTcW(tblWidth);
        }


        for (int i = 0; i < examinationList.size(); i++) {
            Tr row = (Tr) rowObjects.get(i + 2);
            for (int j = 0; j < widthsToSet.size(); j++) {
                Tc cell = (Tc) ((JAXBElement) row.getContent().get(j)).getValue();
                TblWidth tblWidth = new TblWidth();
                tblWidth.setW(BigInteger.valueOf(widthsToSet.get(j)));
                cell.getTcPr().setTcW(tblWidth);
            }
        }

        if (IntStream.range(0, pattern.length)
                .filter(i -> pattern[i])
                .count() == 2) {
            for (int i = 0; i < examinationList.size() + 1; i++) {
                Tr row = (Tr) rowObjects.get(i + 1);
                for (int j = 6; j < 8; j++) {
                    Tc cell = (Tc) ((JAXBElement) row.getContent().get(j)).getValue();
                    TblWidth tblWidth = new TblWidth();
                    tblWidth.setW(BigInteger.valueOf(requirementsTitleCellWidth / 2));
                    cell.getTcPr().setTcW(tblWidth);
                }
            }
        }

    }

    private int getTableTotalWidthFromRow(Tr row) {
        BigInteger totalWidth = BigInteger.ZERO;

        for (Object cellObj: row.getContent()) {
            Tc cell = (Tc) ((JAXBElement) cellObj).getValue();
            totalWidth = totalWidth.add(cell.getTcPr().getTcW().getW());
        }

        return totalWidth.intValue();
    }

    private boolean[] getRequirementsPattern() {
        if (examinationList == null) {
            throw new IllegalStateException("ExaminationList not set");
        }

        boolean[] pattern = new boolean[]{false, false, false};

        examinationList.forEach(examination -> {
            if (!examination.getSignage().equals("")) {
                pattern[0] = true;
            }
            if (!examination.getSpecification().equals("")) {
                pattern[1] = true;
            }
            if (!examination.getNutritionalValue().equals("")) {
                pattern[2] = true;
            }
        });

        return pattern;
    }

    private boolean checkIfUncertaintyExists() {
        if (examinationList == null) {
            throw new IllegalStateException("ExaminationList not set");
        }

        for (Examination examination: examinationList) {
            if (examination.getUncertainty() != 0f) {
                return true;
            }
        }

        return false;
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

    public void setParameters(Sample sample) {
        this.sample = sample;
        this.examinationList = examinationRepository.findBySampleId(sample.getId());
    }

}
