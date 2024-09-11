package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.FilterRequest;
import agh.edu.pl.slpbackend.dto.filters.FilterResponse;
import agh.edu.pl.slpbackend.enums.ProgressStatusEnum;
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

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(sampleService.count(), HttpStatus.OK);
    }

    @GetMapping("/{sampleId}")
    public ResponseEntity<SampleDto> getOne(@PathVariable final Long sampleId) {
        SampleDto sampleDto = sampleService.selectOne(sampleId);
        return new ResponseEntity<>(sampleDto, HttpStatus.OK);
    }

    @PutMapping("status/{sampleId}/{status}")
    public ResponseEntity<Sample> updateStatus(@PathVariable final Long sampleId, @PathVariable final String status) {
        return new ResponseEntity<>(sampleService.updateStatus(sampleId, ProgressStatusEnum.convertEnum(status)), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody SampleDto sampleDto) throws Exception {
        return add(sampleDto, sampleService);
    }

    @DeleteMapping("/{sampleId}")
    public ResponseEntity<Void> delete(@PathVariable final Long sampleId) throws Exception {
        return delete(SampleDto.builder().id(sampleId).build(), sampleService);
    }

    @PutMapping("/{sampleId}")
    public ResponseEntity<Void> update(@PathVariable final Long sampleId, @RequestBody SampleDto sampleDto) throws Exception {
        return edit(sampleDto, sampleService);
    }


    @PutMapping("list/filtered")
    public ResponseEntity<FilterResponse> filter(@RequestBody FilterRequest request) {
        return new ResponseEntity<>(sampleService.filter(request), HttpStatus.OK);
    }
}
