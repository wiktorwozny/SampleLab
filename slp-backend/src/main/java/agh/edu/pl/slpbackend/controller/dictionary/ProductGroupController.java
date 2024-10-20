package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ProductGroupDto;
import agh.edu.pl.slpbackend.service.dictionary.ProductGroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product-group")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductGroupController extends AbstractController {

    private final ProductGroupService productGroupService;

    @GetMapping("/list")
    public ResponseEntity<List<ProductGroupDto>> list() {
        try {
            List<ProductGroupDto> list = productGroupService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody ProductGroupDto productGroupDto) throws Exception {
        return add(productGroupDto, productGroupService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody ProductGroupDto productGroupDto) throws Exception {
        return edit(productGroupDto, productGroupService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        return delete(ProductGroupDto.builder().id(id).build(), productGroupService);
    }
}
