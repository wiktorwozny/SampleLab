package agh.edu.pl.slpbackend.database.backup;

import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BackupService {

    private int exportDatabaseToSQL(final BackupModeEnum backupMode) throws IOException, InterruptedException {
        String pgDumpPath = "\"C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe\"";  // Ścieżka do pg_dump
        String host = "sample-lab-db.ct66gugwuj5h.eu-north-1.rds.amazonaws.com";
        String port = "5432";
        String database = "SampleLabDB";
        String user = "postgres";
        String password = "12345678";
        String format = "plain";
//        String home = System.getProperty("user.home");
        String backupFile = "D:\\Studia_AGH\\praca inżynierska\\SampleLab\\slp-backend\\backup_directory\\";

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

        String host = "sample-lab-db.ct66gugwuj5h.eu-north-1.rds.amazonaws.com";
        String port = "5432";
        String database = "SampleLabDB";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        String user = "postgres";
        String password = "12345678";
//        String home = System.getProperty("user.home");
        String outputDirectory = "D:\\Studia_AGH\\praca inżynierska\\SampleLab\\slp-backend\\backup_directory\\"; // Ścieżka do katalogu, gdzie zapisywane będą pliki CSV

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
        Statement stmt = null;
        ResultSet rs = null;
        FileWriter fileWriter = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            fileWriter = new FileWriter(csvFilePath);

            // Zapisz nagłówki kolumn
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(rsmd.getColumnName(i));
                if (i < columnCount) fileWriter.append(",");
            }
            fileWriter.append("\n");

            // Zapisz dane tabeli
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String data = rs.getString(i);
                    if (data != null) {
                        fileWriter.append(data.replaceAll("\"", "\"\"")); // Escape double quotes
                    }
                    if (i < columnCount) fileWriter.append(",");
                }
                fileWriter.append("\n");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (fileWriter != null) fileWriter.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int backupExecutor(final BackupModeEnum backupMode) throws IOException, InterruptedException {
        switch (backupMode) {
            case FULL_BACKUP, DATA_ONLY -> {
                return exportDatabaseToSQL(backupMode);
            }
            case CSV -> {
                return exportDatabaseToCSV();
            }
            default -> {
                return 1;
            }
        }

    }


}
