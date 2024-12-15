package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/code")
@CrossOrigin(origins = "http://localhost:3000")
public class CodeController extends AbstractController {

    private final CodeService codeService;

    @GetMapping("/list")
    public ResponseEntity<List<CodeDto>> list() {
        return ResponseEntity.ok(codeService.selectAll());
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid CodeDto codeDto) {
        return add(codeDto, codeService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid CodeDto codeDto) {
        return edit(codeDto, codeService);
    }

    @PreAuthorize("hasRole('WORKER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return delete(CodeDto.builder().id(id).build(), codeService);
    }
}
