package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.service.ExaminationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/examination")
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationController extends AbstractController {

    private final ExaminationService examinationService;

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody ExaminationDto examinationDto) {
        return add(examinationDto, examinationService);
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

    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/update")
    public ResponseEntity<Void> insertExaminationResults(@RequestBody ExaminationDto updatedExaminationDto) {
        return edit(updatedExaminationDto, examinationService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @DeleteMapping("/delete/{examinationId}")
    public ResponseEntity<Void> deleteExamination(@PathVariable final Long examinationId) {
        return delete(ExaminationDto.builder().id(examinationId).build(), examinationService);
    }

}
