package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.service.MethodService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/methods")
@CrossOrigin(origins = "http://localhost:3000")
public class MethodController {

    private final MethodService methodService;

    @PostMapping("/import")
    public ResponseEntity<Void> importMethods(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            methodService.importMethods(inputStream);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
