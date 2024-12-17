package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ProductsScreenController {
    private final StageManager stageManager;

    @Autowired
    public ProductsScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    @Autowired
    private SidebarController sidebarController;
}
