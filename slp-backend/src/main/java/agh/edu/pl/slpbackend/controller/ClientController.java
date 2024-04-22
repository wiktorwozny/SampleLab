package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController extends AbstractController {

    private final ClientService clientService;

    @GetMapping("/list")
    public ResponseEntity<List<ClientDto>> list() {
        try {
            List<ClientDto> list = clientService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Client> add(@RequestBody ClientDto clientDto) {
        return new ResponseEntity<>(add(clientDto, clientService).getStatusCode()); //TODO nie wiem, trzeba przetestować
    }
}
