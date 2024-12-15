package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.service.dictionary.InspectionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inspection")
@CrossOrigin(origins = "http://localhost:3000")
public class InspectionController extends AbstractController {

    private final InspectionService inspectionService;

    @GetMapping("/list")
    public ResponseEntity<List<InspectionDto>> list() {
        return ResponseEntity.ok(inspectionService.selectAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid InspectionDto inspectionDto) {
        return add(inspectionDto, inspectionService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid InspectionDto inspectionDto) {
        return edit(inspectionDto, inspectionService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return delete(InspectionDto.builder().id(id).build(), inspectionService);
    }
}
