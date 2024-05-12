package agh.edu.pl.slpbackend.generateReport;

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
public class GenerateReportService {

    private final SampleRepository sampleRepository;

    public ResponseEntity<HttpStatus> generateReport(final long reportId) {

        final Optional<Sample> sample = sampleRepository.findById(reportId);
        if (sample.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
