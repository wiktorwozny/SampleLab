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

    @GetMapping("/{sampleId}")
    public ResponseEntity<SampleDto> getOne(@PathVariable final Long sampleId) {
        SampleDto sampleDto = sampleService.selectOne(sampleId);
        return new ResponseEntity<>(sampleDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Sample> add(@RequestBody SampleDto sampleDto) {
        return new ResponseEntity<>(add(sampleDto, sampleService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }

    @PutMapping("/{sampleId}")
    public ResponseEntity<SampleDto> update(@PathVariable final Long sampleId, @RequestBody SampleDto sampleDto) {
        sampleDto.setId(sampleId);
        SampleDto response = sampleService.update(sampleDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{sampleId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable final Long sampleId) {
        SampleDto sampleDto = SampleDto.builder()
                .id(sampleId)
                .build();
        sampleService.delete(sampleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
