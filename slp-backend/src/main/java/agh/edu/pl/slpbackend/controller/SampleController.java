package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.service.SampleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sample") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class SampleController extends AbstractController {

    private final SampleService sampleService;

    @GetMapping("/list")
    public ResponseEntity<List<SampleDto>> list() {
        try {
            List<SampleDto> list = sampleService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-sample/{sampleId}")
    public ResponseEntity<SampleDto> getOne(@PathVariable final Long sampleId) {
        try {
            SampleDto sampleDto = sampleService.selectOne(sampleId);
            if (sampleDto == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(sampleDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Sample> add(@RequestBody SampleDto sampleDto) {
        return new ResponseEntity<>(add(sampleDto, sampleService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }

    @DeleteMapping("/{sampleId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable final Long sampleId) {
        SampleDto sampleDto = SampleDto.builder()
                .id(sampleId)
                .build();
        sampleService.delete(sampleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("{sampleId}/report-data")
//    public ResponseEntity<HttpStatus> addReportData(@PathVariable long sampleId, @RequestBody final ReportDataDto reportData) { // TODO przenieśc do report service
//        sampleService.addReportData(sampleId, reportData);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
