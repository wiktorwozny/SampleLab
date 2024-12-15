package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.service.dictionary.IndicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/indication")
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/{indicationId}")
    public ResponseEntity<IndicationDto> getIndicationById(@PathVariable Long indicationId) {
        IndicationDto indicationDto = indicationService.selectById(indicationId);
        return new ResponseEntity<>(indicationDto, HttpStatus.OK);
    }

    @GetMapping("/sample/{sampleId}")
    public ResponseEntity<List<IndicationDto>> getIndicationsForSample(@PathVariable Long sampleId) {
        List<IndicationDto> indicationDtos = indicationService.selectIndicationsForSample(sampleId);
        return new ResponseEntity<>(indicationDtos, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody IndicationDto indicationDto) throws Exception {
        return add(indicationDto, indicationService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody IndicationDto indicationDto) throws Exception {
        return edit(indicationDto, indicationService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        return delete(IndicationDto.builder().id(id).build(), indicationService);
    }

}
