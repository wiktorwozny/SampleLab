package agh.edu.pl.slpbackend.reports.samplereport;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/generate-report")
@CrossOrigin(origins = "http://localhost:3000")
public class SampleReportGeneratorController {

    private final SampleReportGeneratorService sampleReportGeneratorService;

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/sample-report/{sampleId}/{reportType}")
    public ResponseEntity<InputStreamResource> generate(@PathVariable final Long sampleId, @PathVariable final String reportType) {
        InputStreamResource resource = sampleReportGeneratorService.generateReport(sampleId, reportType);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
