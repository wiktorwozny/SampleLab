package agh.edu.pl.slpbackend.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilePathResolver {
    private static final Logger logger = LoggerFactory.getLogger(FilePathResolver.class);

    private final String templatesPath = "/app/report_templates";

    public Path resolvePath(String fileName) {
        logger.info("Attempting to resolve path for fileName: {}", fileName);
        logger.info("Templates base path: {}", templatesPath);

        if (fileName == null) {
            logger.error("Filename is null");
            throw new IllegalArgumentException("Filename cannot be null");
        }

        Path fullPath = Paths.get(templatesPath, fileName);
        logger.info("Resolved full path: {}", fullPath);

        if (!Files.exists(fullPath)) {
            logger.error("File does not exist: {}", fullPath);
//            throw new IllegalArgumentException("File not found: " + fullPath);
        }

        return fullPath;
    }

    public String getFullPath(String fileName) {
        return resolvePath(fileName).toString();
    }
}
