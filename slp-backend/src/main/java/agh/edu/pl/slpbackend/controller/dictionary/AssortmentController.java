package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.assortment.AssortmentDto;
import agh.edu.pl.slpbackend.dto.assortment.AssortmentSaveDto;
import agh.edu.pl.slpbackend.service.dictionary.AssortmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/assortment")
@CrossOrigin(origins = "http://localhost:3000")
public class AssortmentController extends AbstractController {

    private final AssortmentService assortmentService;

    @GetMapping("/list")
    public ResponseEntity<List<AssortmentDto>> list() {
        try {
            List<AssortmentDto> list = assortmentService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody AssortmentSaveDto assortmentSaveDto) throws Exception {
        return add(assortmentSaveDto, assortmentService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody AssortmentSaveDto assortmentSaveDto) throws Exception {
        return edit(assortmentSaveDto, assortmentService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        return delete(AssortmentDto.builder().id(id).build(), assortmentService);
    }
}
