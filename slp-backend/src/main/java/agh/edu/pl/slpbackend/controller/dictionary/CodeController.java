package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        try {
            List<CodeDto> list = codeService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid CodeDto codeDto) throws Exception {
        return add(codeDto, codeService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid CodeDto codeDto) throws Exception {
        return edit(codeDto, codeService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
        return delete(CodeDto.builder().id(id).build(), codeService);
    }
}
