package agh.edu.pl.slpbackend.database.backup;


import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/backup")
@CrossOrigin(origins = "http://localhost:3000")
public class BackupController {

    private BackupService backupService;

    @GetMapping("/")
    public ResponseEntity<InputStreamResource> backup() throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(backupService.backupExecutor());
    }
}
