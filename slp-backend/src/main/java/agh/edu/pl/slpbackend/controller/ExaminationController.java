package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.service.ExaminationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/examination")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationController extends AbstractController {

    private final ExaminationService examinationService;

    @PostMapping("/save")
    public ResponseEntity<Examination> add(@RequestBody ExaminationDto examinationDto) {
        return new ResponseEntity<>(add(examinationDto, examinationService).getStatusCode());
    }

    @GetMapping("/{examinationId}")
    public ResponseEntity<ExaminationDto> getExaminationById(@PathVariable Long examinationId) {
        ExaminationDto examinationDto = examinationService.selectById(examinationId);
        return new ResponseEntity<>(examinationDto, HttpStatus.OK);
    }

    @GetMapping("/sample/{sampleId}")
    public ResponseEntity<List<ExaminationDto>> getExaminationForSample(@PathVariable Long sampleId) {
        List<ExaminationDto> examinationDtos = examinationService.selectExaminationsForSample(sampleId);
        return new ResponseEntity<>(examinationDtos, HttpStatus.OK);
    }

    @PutMapping("/{examinationId}")
    public ResponseEntity<Examination> insertExaminationResults(@PathVariable Long examinationId, @RequestBody ExaminationDto updatedExaminationDto) {
        examinationService.insertExaminationResults(examinationId, updatedExaminationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{examinationId}")
    public ResponseEntity<Examination> deleteExamination(@PathVariable Long examinationId) {
        examinationService.deleteById(examinationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
