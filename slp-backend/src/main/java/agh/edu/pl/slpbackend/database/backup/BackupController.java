package agh.edu.pl.slpbackend.database.backup;


import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/backup") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class BackupController {

    private BackupService backupService;

    @GetMapping("/{mode}")
    public ResponseEntity<Void> backup(@PathVariable final String mode) {
        try {
            if (backupService.exportDatabaseToSQL(BackupModeEnum.convertEnum(mode)) == 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // nie wiem jaki inny exeption
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
