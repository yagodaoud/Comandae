package com.yagodaoud.comandae.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DatabaseBackupService {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    public void backupDatabase(String backupDirectory) {
        try {
            Path sourceFilePath = extractH2DatabasePath();

            Files.createDirectories(Paths.get(backupDirectory));

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path backupFilePath = Paths.get(backupDirectory,
                    sourceFilePath.getFileName().toString().replace(".mv.db",
                            "_" + timestamp + ".mv.db"));

            Files.copy(sourceFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Backup completed successfully to: " + backupFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to back up database", e);
        }
    }

    private Path extractH2DatabasePath() {
        try {
            if (!dbUrl.contains(":h2:")) {
                throw new IllegalStateException("Not an H2 database URL");
            }

            String[] parts = dbUrl.split(":h2:");
            if (parts.length < 2) {
                throw new IllegalStateException("Invalid H2 database URL");
            }

            String dbPath = dbUrl.replace("jdbc:h2:file:", "")
                    .split(";")[0]
                    .trim();
            System.out.println(dbPath);
            Path sourcePath = Paths.get(dbPath + ".mv.db").toAbsolutePath();

            if (!Files.exists(sourcePath)) {
                throw new IOException("Database file not found: " + sourcePath);
            }

            return sourcePath;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not determine database file path", e);
        }
    }
}