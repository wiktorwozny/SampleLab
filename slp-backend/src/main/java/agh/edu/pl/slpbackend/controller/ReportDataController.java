package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.service.ReportDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/report-data") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class ReportDataController extends AbstractController {

    private final ReportDataService reportDataService;

    @GetMapping("/list")
    public ResponseEntity<List<ReportDataDto>> list() {
        try {
            List<ReportDataDto> list = reportDataService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody final ReportDataDto reportData) throws Exception {
        return add(reportData, reportDataService);
    }

    @DeleteMapping("/{reportDataId}")
    public ResponseEntity<Void> delete(@PathVariable final Long reportDataId) throws Exception {
        return delete(ReportDataDto.builder().id(reportDataId).build(), reportDataService);
    }
}
