package agh.edu.pl.slpbackend.reports.kzwa;

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
public class KZWAReportGeneratorController {

    private final KZWAReportGeneratorService kzwaReportGeneratorService;

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/kzwa-report/{sampleId}")
    public ResponseEntity<InputStreamResource> generate(@PathVariable final Long sampleId) {
        InputStreamResource resource = kzwaReportGeneratorService.generateReport(sampleId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
