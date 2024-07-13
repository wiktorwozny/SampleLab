package agh.edu.pl.slpbackend.database.backup;

import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BackupService {

    public int exportDatabaseToSQL(final BackupModeEnum backupMode) throws IOException, InterruptedException {
        String pgDumpPath = "\"C:\\Program Files\\PostgreSQL\\15\\bin\\pg_dump.exe\"";  // Ścieżka do pg_dump
        String host = "localhost";
        String port = "5432";
        String database = "postgres";
        String user = "postgres";
        String password = "[password]";
        String format = "plain";
        String home = System.getProperty("user.home");
        String backupFile = home + "/Downloads/" + "backup.sql";

        // Tworzenie komendy
        String[] command1 = new String[]{
                pgDumpPath,
                "--host=" + host,
                "--port=" + port,
                "--username=" + user,
                "--format=" + format,
                "--file=" + backupFile,
                database
        };

        String[] command2 = new String[]{
                pgDumpPath,
                "--host=" + host,
                "--port=" + port,
                "--username=" + user,
                "--format=" + format,
                "--data-only",
                "--file=" + backupFile,
                database
        };


        try {
            // Ustawienie zmiennej środowiskowej dla hasła
            ProcessBuilder pb = new ProcessBuilder(BackupModeEnum.FULL_BACKUP.equals(backupMode) ? command1 : command2);
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

    private int exportDatabaseToCSV() {

        String host = "localhost";
        String port = "5432";
        String database = "postgres";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        String user = "postgres";
        String password = "Rk020409";
        String home = System.getProperty("user.home");
        String outputDirectory = home + "/Downloads/"; // Ścieżka do katalogu, gdzie zapisywane będą pliki CSV

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Uzyskiwanie listy tabel
            List<String> tableNames = getTableNames(conn);

            for (String tableName : tableNames) {
                exportTableToCSV(conn, tableName, outputDirectory + tableName + ".csv");
            }

            System.out.println("All tables have been exported to CSV files.");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static List<String> getTableNames(Connection conn) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getTables(null, "public", null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    private static void exportTableToCSV(Connection conn, String tableName, String csvFilePath) {
        String copyQuery = String.format("COPY %s TO '%s' DELIMITER ',' CSV HEADER;", tableName, csvFilePath);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(copyQuery);
            System.out.println("Exported table " + tableName + " to " + csvFilePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
