package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.service.ExaminationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/examination")
public class ExaminationController extends AbstractController {

    private final ExaminationService examinationService;

    @PostMapping("/save")
    public ResponseEntity<Examination> add(@RequestBody ExaminationDto examinationDto) {
        return new ResponseEntity<>(add(examinationDto, examinationService).getStatusCode());
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
