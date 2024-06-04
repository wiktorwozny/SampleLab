package agh.edu.pl.slpbackend.reports.kzwa;

import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class KZWAReportGeneratorService {

    private final SampleRepository sampleRepository;
    private final KZWAReportGenerator kzwaReportGenerator;

    public ResponseEntity<HttpStatus> generateReport(final Long sampleId) throws Exception {

        final Optional<Sample> sample = sampleRepository.findById(sampleId);
        if (sample.isPresent()) {
            kzwaReportGenerator.setParameters(sample.get());
            kzwaReportGenerator.generateReport();
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
