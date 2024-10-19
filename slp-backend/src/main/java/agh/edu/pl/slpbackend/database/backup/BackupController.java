package agh.edu.pl.slpbackend.database.backup;


import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/backup") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class BackupController {

    private BackupService backupService;

    @GetMapping("/{mode}")
    public ResponseEntity<InputStreamResource> backup(@PathVariable final String mode) {
        try {
            BackupModeEnum backupMode = BackupModeEnum.convertEnum(mode);

            InputStreamResource resource = backupService.backupExecutor(backupMode);
            if (resource != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<String> importBackup(@RequestParam("file") MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Niepoprawna nazwa pliku.");
            }

            if (originalFileName.endsWith(".zip")) {
                backupService.importBackup(file, BackupModeEnum.CSV);
                return ResponseEntity.ok("Import pliku ZIP zakończony sukcesem.");
            } else if (originalFileName.endsWith(".sql")) {
                backupService.importBackup(file, BackupModeEnum.FULL_BACKUP);
                return ResponseEntity.ok("Import pliku SQL zakończony sukcesem.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nieobsługiwany format pliku. Użyj pliku .sql lub .zip.");
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas importu: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
