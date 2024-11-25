package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.model.Inspection;
import agh.edu.pl.slpbackend.service.dictionary.InspectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inspection") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class InspectionController extends AbstractController {

    private final InspectionService inspectionService;

    @GetMapping("/list")
    public ResponseEntity<List<InspectionDto>> list() {
        try {
            List<InspectionDto> list = inspectionService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Inspection> add(@RequestBody InspectionDto inspectionDto) throws Exception {
        return new ResponseEntity<>(add(inspectionDto, inspectionService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody InspectionDto inspectionDto) throws Exception {
        return edit(inspectionDto, inspectionService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        return delete(InspectionDto.builder().id(id).build(), inspectionService);
    }
}
