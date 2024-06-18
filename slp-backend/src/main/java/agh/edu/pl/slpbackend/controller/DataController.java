package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.dto.filters.Filters;
import agh.edu.pl.slpbackend.service.CodeService;
import agh.edu.pl.slpbackend.service.DataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/data")
@CrossOrigin(origins = "http://localhost:3000")
public class DataController {
    DataService dataService;

    @GetMapping("/filters")
    public ResponseEntity<Filters> getFilters() {
        return new ResponseEntity<>(dataService.getFilters(), HttpStatus.OK);
    }
}