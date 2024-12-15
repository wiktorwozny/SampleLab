package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupDto;
import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupSaveDto;
import agh.edu.pl.slpbackend.service.dictionary.ProductGroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
        return ResponseEntity.ok(productGroupService.selectAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid ProductGroupSaveDto productGroupSaveDto) {
        return add(productGroupSaveDto, productGroupService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid ProductGroupSaveDto productGroupSaveDto) {
        return edit(productGroupSaveDto, productGroupService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return delete(ProductGroupDto.builder().id(id).build(), productGroupService);
    }
}
