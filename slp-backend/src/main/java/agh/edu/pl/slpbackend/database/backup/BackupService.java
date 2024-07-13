package agh.edu.pl.slpbackend.database.backup;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
@AllArgsConstructor
public class BackupService {

    public int exportDatabase() throws IOException, InterruptedException {
        String pgDumpPath = "\"C:\\Program Files\\PostgreSQL\\15\\bin\\pg_dump.exe\"";  // Ścieżka do pg_dump
        String host = "localhost";
        String port = "5432";
        String database = "postgres";
        String user = "postgres";
        String password = "[password]";
        String home = System.getProperty("user.home");
        String backupFile = home + "/Downloads/" + "backup.sql";

        // Tworzenie komendy
        String[] command = new String[]{
                pgDumpPath,
                "--host=" + host,
                "--port=" + port,
                "--username=" + user,
                "--format=plain",
                "--data-only",
                "--file=" + backupFile,
                database
        };

        try {
            // Ustawienie zmiennej środowiskowej dla hasła
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.environment().put("PGPASSWORD", password);
            pb.redirectErrorStream(true);

            // Uruchomienie procesu
            Process process = pb.start();

            // Odczyt wyjścia procesu
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Czekanie na zakończenie procesu
            return process.waitFor();
        } catch (Exception e) {
            throw e;
        }
    }
}
