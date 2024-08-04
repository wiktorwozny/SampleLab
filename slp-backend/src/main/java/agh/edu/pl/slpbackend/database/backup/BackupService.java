package agh.edu.pl.slpbackend.database.backup;

import agh.edu.pl.slpbackend.enums.BackupModeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        // Tworzenie komendy
        String[] command1 = new String[]{
                pgDumpPath,
                "--host=" + host,
                "--port=" + port,
                "--username=" + user,
                "--format=" + format,
                database
        };

        String[] command2 = new String[]{
                pgDumpPath,
                "--host=" + host,
                "--port=" + port,
                "--username=" + user,
                "--format=" + format,
                "--data-only",
                database
        };

        ProcessBuilder pb = new ProcessBuilder(BackupModeEnum.FULL_BACKUP.equals(backupMode) ? command1 : command2);
        pb.environment().put("PGPASSWORD", password);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = process.getInputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
        }

        // Czekanie na zakończenie procesu
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("Backup process failed with exit code " + exitCode);
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        return new InputStreamResource(byteArrayInputStream);
    }

    public InputStreamResource exportDatabaseToCSV() throws IOException {
        String host = "sample-lab-db.ct66gugwuj5h.eu-north-1.rds.amazonaws.com";
        String port = "5432";
        String database = "SampleLabDB";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        String user = "postgres";
        String password = "12345678";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Uzyskiwanie listy tabel
            List<String> tableNames = getTableNames(conn);

            // Tworzenie pliku ZIP w pamięci
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
                for (String tableName : tableNames) {
                    exportTableToCSVInMemory(conn, tableName, zos);
                }
            }

            System.out.println("All tables have been exported to CSV files and zipped.");

            // Odczyt ZIP do InputStreamResource
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new InputStreamResource(byteArrayInputStream);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Error exporting database to CSV.", e);
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

    private static void exportTableToCSVInMemory(Connection conn, String tableName, ZipOutputStream zos) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ZipEntry zipEntry = new ZipEntry(tableName + ".csv");
            zos.putNextEntry(zipEntry);

            // Zapisz nagłówki kolumn
            for (int i = 1; i <= columnCount; i++) {
                zos.write(rsmd.getColumnName(i).getBytes());
                if (i < columnCount) zos.write(",".getBytes());
            }
            zos.write("\n".getBytes());

            // Zapisz dane tabeli
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String data = rs.getString(i);
                    if (data != null) {
                        zos.write(data.replaceAll("\"", "\"\"").getBytes()); // Escape double quotes
                    }
                    if (i < columnCount) zos.write(",".getBytes());
                }
                zos.write("\n".getBytes());
            }

            zos.closeEntry();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
