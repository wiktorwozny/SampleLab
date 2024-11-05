package agh.edu.pl.slpbackend.reports.samplereport;

import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@Slf4j
@AllArgsConstructor
public class SampleReportGeneratorService {

    private final SampleRepository sampleRepository;
    private final SampleReportGenerator sampleReportGenerator;

    public InputStreamResource generateReport(final Long sampleId, String reportType) {
        final Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(SampleNotFoundException::new);

        sampleReportGenerator.setParameters(sample, reportType);

        ByteArrayOutputStream byteArrayOutputStream = sampleReportGenerator.generateReport();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return new InputStreamResource(inputStream);
    }
}
