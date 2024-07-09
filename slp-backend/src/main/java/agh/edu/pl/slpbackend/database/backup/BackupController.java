package agh.edu.pl.slpbackend.database.backup;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/backup") //TODO odpowiedni rooting jeszcze nie wiem XDD
@CrossOrigin(origins = "http://localhost:3000")
public class BackupController {

    private BackupService backupService;

    @GetMapping("/")
    public ResponseEntity<Void> backup() {
        try {
            if (backupService.exportDatabase() == 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // nie wiem jaki inny exeption
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
