package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.model.ReportData;
import agh.edu.pl.slpbackend.service.ReportDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/report-data") //TODO odpowiedni rooting jeszcze nie wiem XDD
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
    public ResponseEntity<ReportData> add(@RequestBody ReportDataDto reportDataDto) {
        return new ResponseEntity<>(add(reportDataDto, reportDataService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }
}
