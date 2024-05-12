package agh.edu.pl.slpbackend.generateReport;


import agh.edu.pl.slpbackend.model.Sample;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GenerateReport {
    private HashMap<String, String> fieldsMap = new HashMap<String, String>();

    public void generateReport(final Sample sample) throws Exception {
        try (FileInputStream filePath = new FileInputStream("report_templates/test.docx")) {
            HashMap<String, String> fieldsMap = getStringStringHashMap(sample);

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
        fieldsMap.put("${probkaId}", "jekieś IDK XDD");
        fieldsMap.put("${newDate}", getNewDate());
        fieldsMap.put("${counter}", Integer.toString(1));
        return fieldsMap;
    }

    private String getNewDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
