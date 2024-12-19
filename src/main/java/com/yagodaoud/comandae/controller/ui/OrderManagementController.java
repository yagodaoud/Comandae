package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderManagementController {

    private final StageManager stageManager;

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @Autowired
    public OrderManagementController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void initialize() {
        sidebarController.setSelectedScreen(NavigationScreen.ORDER_MANAGEMENT);
    }

}