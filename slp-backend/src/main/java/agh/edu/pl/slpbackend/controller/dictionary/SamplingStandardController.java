package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.service.dictionary.SamplingStandardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sampling-standard") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class SamplingStandardController extends AbstractController {

    private final SamplingStandardService samplingStandardService;

    @GetMapping("/list")
    public ResponseEntity<List<SamplingStandardDto>> list() {
        try {
            List<SamplingStandardDto> list = samplingStandardService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid SamplingStandardDto samplingStandardDto) throws Exception {
        return add(samplingStandardDto, samplingStandardService);
    }


    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid SamplingStandardDto samplingStandardDto) throws Exception {
        return edit(samplingStandardDto, samplingStandardService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        return delete(SamplingStandardDto.builder().id(id).build(), samplingStandardService);
    }
}
