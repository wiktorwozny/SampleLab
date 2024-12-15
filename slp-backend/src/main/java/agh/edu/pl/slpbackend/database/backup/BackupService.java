package agh.edu.pl.slpbackend.database.backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class BackupService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;


    private InputStreamResource exportDatabaseToCSV() throws IOException {

        String url = dbUrl;
        String user = dbUsername;
        String password = dbPassword;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            List<String> tableNames = getTableNames(conn);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
                for (String tableName : tableNames) {
                    exportTableToCSVInMemory(conn, tableName, zos);
                }
            }

            System.out.println("All tables have been exported to CSV files and zipped.");

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new InputStreamResource(byteArrayInputStream);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Error exporting database to CSV.", e);
        }
    }

    private List<String> getTableNames(Connection conn) throws SQLException {
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

    private void exportTableToCSVInMemory(Connection conn, String tableName, ZipOutputStream zos) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ZipEntry zipEntry = new ZipEntry(tableName + ".csv");
            zos.putNextEntry(zipEntry);

            for (int i = 1; i <= columnCount; i++) {
                zos.write(rsmd.getColumnName(i).getBytes());
                if (i < columnCount) zos.write(",".getBytes());
            }
            zos.write("\n".getBytes());

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


    public InputStreamResource backupExecutor() throws IOException {
        return exportDatabaseToCSV();
    }


}
