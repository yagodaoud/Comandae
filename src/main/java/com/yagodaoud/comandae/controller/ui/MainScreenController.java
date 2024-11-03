package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MainScreenController {

    private final StageManager stageManager;

    @Autowired
    public MainScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    public void goToMenu() {
        stageManager.switchScene("view/MenuScreen.fxml", "Menu Screen");
    }

    @FXML
    public void goToOrderManagement() {
        stageManager.switchScene("view/OrderManagementScreen.fxml", "Order Management");

    }
}