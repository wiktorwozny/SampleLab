package agh.edu.pl.slpbackend.reports.kzwa;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/generate-report")
@CrossOrigin(origins = "http://localhost:3000")
public class KZWAReportGeneratorController {

    private final KZWAReportGeneratorService kzwaReportGeneratorService;

    @PostMapping("/kzwa-report/{sampleId}")
    public ResponseEntity<HttpStatus> generate(@PathVariable final Long sampleId) {
        try {
            return kzwaReportGeneratorService.generateReport(sampleId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
