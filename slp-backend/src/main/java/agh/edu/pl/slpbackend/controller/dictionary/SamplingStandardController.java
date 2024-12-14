package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.service.dictionary.SamplingStandardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sampling-standard")
@CrossOrigin(origins = "http://localhost:3000")
public class SamplingStandardController extends AbstractController {

    private final SamplingStandardService samplingStandardService;

    @GetMapping("/list")
    public ResponseEntity<List<SamplingStandardDto>> list() {
        return ResponseEntity.ok(samplingStandardService.selectAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody SamplingStandardDto samplingStandardDto) {
        return add(samplingStandardDto, samplingStandardService);
    }


    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody SamplingStandardDto samplingStandardDto) {
        return edit(samplingStandardDto, samplingStandardService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return delete(SamplingStandardDto.builder().id(id).build(), samplingStandardService);
    }
}
