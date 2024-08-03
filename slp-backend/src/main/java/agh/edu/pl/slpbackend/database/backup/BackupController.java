package agh.edu.pl.slpbackend.database.backup;


import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/backup") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class BackupController {

    private BackupService backupService;

    @GetMapping("/{mode}")
    public ResponseEntity<InputStreamResource> backup(@PathVariable final String mode) {
        try {
            if (backupService.backupExecutor(BackupModeEnum.convertEnum(mode)) != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(backupService.backupExecutor(BackupModeEnum.convertEnum(mode)));
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // nie wiem jaki inny exeption
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
