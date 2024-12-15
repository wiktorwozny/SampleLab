package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.database.backup.BackupController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BackupTest {

    @Autowired
    private BackupController controller;

    @Test
    void csv_backup() throws IOException {
        var response = controller.backup();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
