package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class HeaderController {

    private final StageManager stageManager;

    @FXML
    public TextField searchField;

    @FXML
    private Text appTitle;

    @Autowired
    public HeaderController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    private void handleNotificationIconClick() {
        // Handle notification icon click
        System.out.println("Notification icon clicked!");
    }

    private void handleProfileIconClick() {
        // Handle profile icon click
        System.out.println("Profile icon clicked!");
    }

    private void handleSettingsIconClick() {
        // Handle settings icon click
        System.out.println("Settings icon clicked!");
    }
}
