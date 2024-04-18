package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.service.SampleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api") //TODO odpowiedni rooting jeszcze nie wiem XDD
public class SampleController extends AbstractController {

    private final SampleService sampleService;

    @PostMapping("/save")
    public ResponseEntity<Sample> add(@RequestBody SampleDto sampleDto) {
        return new ResponseEntity<>(add(sampleDto, sampleService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }
}
