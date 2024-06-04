package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.service.SamplingStandardService;
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
    public ResponseEntity<Void> add(@RequestBody SamplingStandardDto samplingStandardDto) throws Exception {
        return add(samplingStandardDto, samplingStandardService);
    }
}
