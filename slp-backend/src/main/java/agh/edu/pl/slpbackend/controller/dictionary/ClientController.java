package agh.edu.pl.slpbackend.controller.dictionary;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.service.dictionary.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController extends AbstractController {

    private final ClientService clientService;

    @GetMapping("/list")
    public ResponseEntity<List<ClientDto>> list() {
        return ResponseEntity.ok(clientService.selectAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> add(@RequestBody @Valid ClientDto clientDto) {
        return add(clientDto, clientService);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> edit(@RequestBody @Valid ClientDto clientDto) {
        return edit(clientDto, clientService);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return delete(ClientDto.builder().id(id).build(), clientService);
    }
}
