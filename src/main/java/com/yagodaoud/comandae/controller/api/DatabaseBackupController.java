package com.yagodaoud.comandae.controller.api;

import com.yagodaoud.comandae.service.DatabaseBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseBackupController {

    @Autowired
    private final DatabaseBackupService databaseBackupService;

    public DatabaseBackupController(DatabaseBackupService databaseBackupService) {
        this.databaseBackupService = databaseBackupService;
    }

    @GetMapping("/api/backup")
    public String backupDatabaseToScript(@RequestParam String filePath) {
        try {
            databaseBackupService.backupDatabase(filePath);
            return "Backup completed successfully. File saved to: " + filePath;
        } catch (Exception e) {
            return "Backup failed: " + e.getMessage();
        }
    }
}