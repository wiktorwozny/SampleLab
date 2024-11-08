package agh.edu.pl.slpbackend.reports;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Component
public class FilePathResolver {
    @Value("${app.templates.path:/app/report_templates}")
    private String templatesPath;

    public Path resolvePath(String fileName) {
        return Paths.get(templatesPath, fileName);
    }

    public String getFullPath(String fileName) {
        return resolvePath(fileName).toString();
    }
}
