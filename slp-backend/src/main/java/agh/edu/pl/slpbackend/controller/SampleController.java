package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.service.SampleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api") //TODO odpowiedni rooting jeszcze nie wiem XDD
public class SampleController {

    private final SampleService sampleService;

}
