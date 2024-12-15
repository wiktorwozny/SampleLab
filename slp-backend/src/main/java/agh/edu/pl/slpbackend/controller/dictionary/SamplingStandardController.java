package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.service.dictionary.SamplingStandardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid SamplingStandardDto samplingStandardDto) {
        return add(samplingStandardDto, samplingStandardService);
    }


    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid SamplingStandardDto samplingStandardDto) {
        return edit(samplingStandardDto, samplingStandardService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return delete(SamplingStandardDto.builder().id(id).build(), samplingStandardService);
    }
}
