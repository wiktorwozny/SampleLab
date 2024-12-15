package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.service.ReportDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/report-data")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportDataController extends AbstractController {

    private final ReportDataService reportDataService;

    @GetMapping("/list")
    public ResponseEntity<List<ReportDataDto>> list() {
        return ResponseEntity.ok(reportDataService.selectAll());
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody final ReportDataDto reportData) {
        return add(reportData, reportDataService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @DeleteMapping("/{reportDataId}")
    public ResponseEntity<Void> delete(@PathVariable final Long reportDataId) {
        return delete(ReportDataDto.builder().id(reportDataId).build(), reportDataService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody ReportDataDto reportDataDto) {
        return edit(reportDataDto, reportDataService);
    }
    @GetMapping("/sample/{sampleId}")
    public ResponseEntity<ReportDataDto> getReportBySampleId(@PathVariable final Long sampleId) {
        return new ResponseEntity<>(reportDataService.selectBySampleId(sampleId), HttpStatus.OK);
    }

}
