package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.iface.AbstractController;
import agh.edu.pl.slpbackend.dto.AddressDto;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/address") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class AddressController extends AbstractController {
    private AddressService addressService;

    @GetMapping("/list")
    public ResponseEntity<List<AddressDto>> list() {
        try {
            List<AddressDto> list = addressService.selectAll();

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
