package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MenuScreenController {

    private final StageManager stageManager;

    @Autowired
    public MenuScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @FXML
    public void initialize() {
        sidebarController.setSelectedScreen(NavigationScreen.MENU);
    }
}
