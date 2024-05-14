package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.service.IndicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/indication")
public class IndicationController extends AbstractController {

    private final IndicationService indicationService;

    @GetMapping("/list")
    public ResponseEntity<List<IndicationDto>> list() {
        try {
            List<IndicationDto> list = indicationService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{sampleId}")
    public ResponseEntity<List<IndicationDto>> getIndicationsForSample(@PathVariable Long sampleId) {
        List<IndicationDto> indicationDtos = indicationService.selectIndicationsForSample(sampleId);
        return new ResponseEntity<>(indicationDtos, HttpStatus.OK);
    }

}
