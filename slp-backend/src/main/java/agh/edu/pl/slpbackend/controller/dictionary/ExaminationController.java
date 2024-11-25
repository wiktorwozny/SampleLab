package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ExaminationDto;
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
    public ResponseEntity<Void> add(@RequestBody ExaminationDto examinationDto) throws Exception {
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

    @PutMapping("/update")
    public ResponseEntity<Void> insertExaminationResults(@RequestBody ExaminationDto updatedExaminationDto) throws Exception {
        return edit(updatedExaminationDto, examinationService);
    }

    @DeleteMapping("/delete/{examinationId}")
    public ResponseEntity<Void> deleteExamination(@PathVariable final Long examinationId) throws Exception {
        return delete(ExaminationDto.builder().id(examinationId).build(), examinationService);
    }

}
