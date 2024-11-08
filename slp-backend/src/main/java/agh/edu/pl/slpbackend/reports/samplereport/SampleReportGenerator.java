package agh.edu.pl.slpbackend.reports.samplereport;


import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.reports.FilePathResolver;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleReportGenerator {

    @NonNull
    private final ExaminationRepository examinationRepository;
    private Sample sample;
    private List<Examination> examinationList;
    private List<Examination> basicExaminationList;
    private String reportTemplatePathName;
    private final FilePathResolver pathResolver = new FilePathResolver();

    public ByteArrayOutputStream generateReport() {
        if (sample == null) {
            throw new IllegalStateException("Sample not set");
        }

        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                    .load(new java.io.File(pathResolver.getFullPath(this.reportTemplatePathName)));

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

            // base examination table
            boolean[] pattern = getRequirementsPattern();
            XLSXtoTblConverter xlsXtoTblConverter = new XLSXtoTblConverter(pattern, uncertaintyExists, examinationList);
            Tbl convertedExaminationsTable = xlsXtoTblConverter.convert();

            if (convertedExaminationsTable != null) {
                adjustExaminationsTableSize(convertedExaminationsTable, pattern);
                addTableAtParagraph(documentPart, "EXAMINATIONS_TABLE", convertedExaminationsTable);
            }

            // organoleptic examination table
            XLSXorganolepticToTblConverter xlsXorganolepticToTblConverter = new XLSXorganolepticToTblConverter(examinationList, this.sample.getAssortment().getOrganolepticMethod());
            Tbl convertedOrganolepticExaminationsTable = xlsXorganolepticToTblConverter.convert();

            if (convertedOrganolepticExaminationsTable != null) {
                addTableAtParagraph(documentPart, "ORGANOLEPTIC_TABLE", convertedOrganolepticExaminationsTable);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Docx4J.save(wordMLPackage, byteArrayOutputStream);

            xlsXtoTblConverter.cleanFiles();
            xlsXorganolepticToTblConverter.cleanFiles();

            return byteArrayOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Map<String, String> getStringStringHashMap(boolean uncertaintyExists) {
        Map<String, String> fieldsMap = new HashMap<>();

        //  analysis
        if (sample.isAnalysis()) {
            fieldsMap.put("analysis1", "Analiza odwoławcza");
            fieldsMap.put("analysis2", "");
        } else {
            fieldsMap.put("analysis1", "Sprawozdanie z badań nr …/ …/ rok/ lab");
            fieldsMap.put("analysis2", "Aneks do / Korekta Sprawozdania z badań nr …/ …/ rok/ lab z dnia ...");
        }

        // F4 template
        fieldsMap.put("protocolNumber", sample.getReportData().getProtocolNumber() != null ? sample.getReportData().getProtocolNumber() : "");  // TODO add field
        fieldsMap.put("controllersIndication", formatRecipient(sample));
        fieldsMap.put("collectionDate", sample.getReportData().getCollectionDate() != null ? formatLocalDate(sample.getReportData().getCollectionDate()) : "");  // TODO add field
        fieldsMap.put("manufacturer", formatManufacturer(sample));
        fieldsMap.put("recipient", formatRecipient(sample));
        fieldsMap.put("supplier", formatSupplier(sample));
        fieldsMap.put("manufacturerCountry", sample.getReportData().getManufacturerCountry() != null ? sample.getReportData().getManufacturerCountry() : "");
        fieldsMap.put("samplingStandard", sample.getSamplingStandard().getName());
        fieldsMap.put("assortment", sample.getAssortment().getName());
        fieldsMap.put("batchSize", sample.getReportData().getBatchSizeProd() != null ? sample.getReportData().getBatchSizeProd() : (sample.getReportData().getBatchSizeStorehouse() != null ? sample.getReportData().getBatchSizeStorehouse() : ""));
        fieldsMap.put("batchNumber", sample.getReportData().getBatchNumber() != 0 ? String.valueOf(sample.getReportData().getBatchNumber()) : "");
        fieldsMap.put("productionDate", sample.getReportData().getProductionDate() != null ? sample.getReportData().getProductionDate().toString() : "");
        fieldsMap.put("expirationDate", sample.getExpirationDate().toString());
        fieldsMap.put("expirationComment", sample.getExpirationComment());
        fieldsMap.put("clientName", sample.getClient().getName());
        fieldsMap.put("clientAddress", formatClientsAddress(sample.getClient().getAddress()));
        fieldsMap.put("admissionDate", formatLocalDate(sample.getAdmissionDate()));
        fieldsMap.put("sampleSize", sample.getSize());
        fieldsMap.put("samplePacking", sample.getReportData().getSamplePacking() != null ? sample.getReportData().getSamplePacking() : "");
        fieldsMap.put("sampleState", sample.getState() != null ? sample.getState() : "bez zastrzeżeń");

        //  F5 template
        fieldsMap.put("jobNumber", sample.getReportData().getJobNumber() != null ? sample.getReportData().getJobNumber().toString() : "");
        fieldsMap.put("mechanism", sample.getReportData().getMechanism() != null ? sample.getReportData().getMechanism() : "");

        // uncertainty
        if (uncertaintyExists) {
            fieldsMap.put("uncertaintyInfo1", "Podana niepewność jest niepewnością rozszerzoną, uzyskaną przez pomnożenie niepewności standardowej\n" +
                    " i współczynnika rozszerzenia k=2, co w przybliżeniu zapewnia poziom ufności 95%.");
            fieldsMap.put("uncertaintyInfo2", "Podana niepewność pomiaru oszacowana została tylko i wyłącznie dla podanej metodyki badawczej.");
        } else {
            fieldsMap.put("uncertaintyInfo1", "");
            fieldsMap.put("uncertaintyInfo2", "");
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

        examinationList.sort(Comparator.comparing(Examination::getStartDate)
                .thenComparing(Examination::getEndDate)
                .thenComparing(exam -> exam.getIndication().getId()));

        int lpCounter = 1;
        Map<String, List<Integer>> mergedExaminationsMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate organolepticStartDate = null;
        LocalDate organolepticEndDate = null;
        boolean hasOrganoleptic = false;

        for (Examination examination : examinationList) {

            if (examination.getIndication().isOrganoleptic()) {
                hasOrganoleptic = true;
                LocalDate startDate = examination.getStartDate();
                LocalDate endDate = examination.getEndDate();

                if (startDate != null) {
                    organolepticStartDate = (organolepticStartDate == null || startDate.isBefore(organolepticStartDate))
                            ? startDate
                            : organolepticStartDate;
                }

                if (endDate != null) {
                    organolepticEndDate = (organolepticEndDate == null || endDate.isAfter(organolepticEndDate))
                            ? endDate
                            : organolepticEndDate;
                }

                continue;
            }

            LocalDate startDate = examination.getStartDate();
            LocalDate endDate = examination.getEndDate();

            if (startDate == null || endDate == null) {
                continue;
            }

            String key = startDate.format(formatter) + ";" + endDate.format(formatter);

            mergedExaminationsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(lpCounter);

            lpCounter++;
        }

        for (Map.Entry<String, List<Integer>> entry : mergedExaminationsMap.entrySet()) {
            String[] dates = entry.getKey().split(";");
            LocalDate startDate = LocalDate.parse(dates[0]);
            LocalDate endDate = LocalDate.parse(dates[1]);

            String lpList = entry.getValue().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            addRowToTable(examinationsTimesTable, formatLocalDate(startDate), lpList, formatLocalDate(endDate), lpList);
        }

        if (hasOrganoleptic && organolepticStartDate != null && organolepticEndDate != null) {
            String lpList = String.valueOf(lpCounter);
            addRowToTable(examinationsTimesTable, formatLocalDate(organolepticStartDate), lpList, formatLocalDate(organolepticEndDate), lpList);
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


        for (int i = 0; i < basicExaminationList.size(); i++) {
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
            for (int i = 0; i < basicExaminationList.size() + 1; i++) {
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

        basicExaminationList.forEach(examination -> {
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

    private String formatClientsAddress(Address address) {
        return "ul. " + address.getStreet() + "\n" + address.getZipCode() + ", " + address.getCity();
    }

    private String formatAddress(Address address) {
        return "ul. " + address.getStreet() + " " + address.getZipCode() + ", " + address.getCity();
    }

    private String formatManufacturer(Sample sample) {
        return sample.getReportData().getManufacturerName() + " " + formatAddress(sample.getReportData().getManufacturerAddress());
    }

    private String formatRecipient(Sample sample) {
        return sample.getReportData().getRecipientName() + " " + formatAddress(sample.getReportData().getRecipientAddress());
    }

    private String formatSupplier(Sample sample) {
        return sample.getReportData().getSupplierName() + " " + formatAddress(sample.getReportData().getSupplierAddress());
    }

    private String addLineBreaks(String... lines) {
        return String.join("<w:br/>", lines);
    }

    public void setParameters(Sample sample, String reportType) {
        this.sample = sample;
        this.examinationList = examinationRepository.findBySampleId(sample.getId());
        this.basicExaminationList = examinationList.stream()
                .filter(exam -> exam.getIndication() != null && Boolean.FALSE.equals(exam.getIndication().isOrganoleptic()))
                .collect(Collectors.toList());
        if ("F5".equals(reportType)) {
            this.reportTemplatePathName = "report_templates/F-5_report_template.docx";
        } else {
            this.reportTemplatePathName = "report_templates/F-4_report_template.docx";
        }
    }

}
