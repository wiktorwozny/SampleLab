package agh.edu.pl.slpbackend.database.backup;

import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@AllArgsConstructor
public class BackupService {

    public InputStreamResource exportDatabaseToSQL(final BackupModeEnum backupMode) throws IOException, InterruptedException {
        String pgDumpPath = "\"C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe\"";  // Ścieżka do pg_dump
        String host = "sample-lab-db.ct66gugwuj5h.eu-north-1.rds.amazonaws.com";
        String port = "5432";
        String database = "SampleLabDB";
        String user = "postgres";
        String password = "12345678";
        String format = "plain";
        String backupFile = "D:\\Studia_AGH\\praca inżynierska\\SampleLab\\slp-backend\\backup_directory\\backup.sql";

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

        ProcessBuilder pb = new ProcessBuilder(BackupModeEnum.FULL_BACKUP.equals(backupMode) ? command1 : command2);
        pb.environment().put("PGPASSWORD", password);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Czekanie na zakończenie procesu
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("Backup process failed with exit code " + exitCode);
        }

        // Odczyt pliku backup.sql do InputStreamResource
        File file = new File(backupFile);
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        if (!file.delete()) {
            System.err.println("Failed to delete the backup file: " + backupFile);
        }

        return new InputStreamResource(byteArrayInputStream);
    }

    public InputStreamResource exportDatabaseToCSV() throws IOException {
        String host = "sample-lab-db.ct66gugwuj5h.eu-north-1.rds.amazonaws.com";
        String port = "5432";
        String database = "SampleLabDB";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        String user = "postgres";
        String password = "12345678";
        String outputDirectory = "D:\\Studia_AGH\\praca inżynierska\\SampleLab\\slp-backend\\backup_directory\\";
        String zipFilePath = outputDirectory + "backup.zip"; // Ścieżka do pliku ZIP

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Uzyskiwanie listy tabel
            List<String> tableNames = getTableNames(conn);

            // Eksportowanie tabel do plików CSV
            for (String tableName : tableNames) {
                exportTableToCSV(conn, tableName, outputDirectory + tableName + ".csv");
            }

            // Pakowanie plików CSV do ZIP
            zipCSVFiles(outputDirectory, zipFilePath, tableNames);

            System.out.println("All tables have been exported to CSV files and zipped.");

            // Odczyt ZIP do InputStreamResource
            return new InputStreamResource(new FileInputStream(zipFilePath));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Error exporting database to CSV.", e);
        } finally {
            // Usunięcie zawartości katalogu backup_directory po zakończeniu
            deleteBackupDirectoryContents(outputDirectory);
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

    private static void zipCSVFiles(String outputDirectory, String zipFilePath, List<String> tableNames) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String tableName : tableNames) {
                String csvFilePath = outputDirectory + tableName + ".csv";
                File csvFile = new File(csvFilePath);
                try (FileInputStream fis = new FileInputStream(csvFile)) {
                    ZipEntry zipEntry = new ZipEntry(tableName + ".csv");
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                }
            }
        }
    }

    private void deleteBackupDirectoryContents(String outputDirectory) {
        File directory = new File(outputDirectory);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete the file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public InputStreamResource backupExecutor(final BackupModeEnum backupMode) throws IOException, InterruptedException {
        switch (backupMode) {
            case FULL_BACKUP, DATA_ONLY -> {
                return exportDatabaseToSQL(backupMode);
            }
            case CSV -> {
                return exportDatabaseToCSV();
            }
            default -> {
                return null;
            }
        }

    }


}
