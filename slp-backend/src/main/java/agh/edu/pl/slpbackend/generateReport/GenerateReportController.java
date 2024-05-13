package agh.edu.pl.slpbackend.generateReport;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/generate-report") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class GenerateReportController {

    private final GenerateReportService generateReportService;

    @GetMapping("/generate/{reportId}")
    public ResponseEntity<HttpStatus> list(@PathVariable long reportId) {
        try {
            return generateReportService.generateReport(reportId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
