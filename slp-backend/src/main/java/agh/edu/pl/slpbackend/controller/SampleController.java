package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.FilterRequest;
import agh.edu.pl.slpbackend.dto.filters.FilterResponse;
import agh.edu.pl.slpbackend.enums.ProgressStatus;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.service.SampleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sample")
@CrossOrigin(origins = "http://localhost:3000")
public class SampleController extends AbstractController {

    private final SampleService sampleService;

    @GetMapping("/list")
    public ResponseEntity<List<SampleDto>> list() {
        return ResponseEntity.ok(sampleService.selectAll());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(sampleService.count());
    }

    @GetMapping("/{sampleId}")
    public ResponseEntity<SampleDto> getOne(@PathVariable final Long sampleId) {
        return ResponseEntity.ok(sampleService.selectOne(sampleId));
    }

    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("status/{sampleId}/{status}")
    public ResponseEntity<Sample> updateStatus(@PathVariable final Long sampleId, @PathVariable final String status) {
        return ResponseEntity.ok(sampleService.updateStatus(sampleId, ProgressStatus.convertEnum(status)));
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody SampleDto sampleDto) {
        return add(sampleDto, sampleService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @DeleteMapping("/{sampleId}")
    public ResponseEntity<Void> delete(@PathVariable final Long sampleId) {
        return delete(SampleDto.builder().id(sampleId).build(), sampleService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/{sampleId}")
    public ResponseEntity<Void> update(@PathVariable final Long sampleId, @RequestBody SampleDto sampleDto) {
        return edit(sampleDto, sampleService);
    }


    @PutMapping("list/filtered")
    public ResponseEntity<FilterResponse> filter(@RequestBody FilterRequest request) {
        return ResponseEntity.ok(sampleService.filter(request));
    }
}
