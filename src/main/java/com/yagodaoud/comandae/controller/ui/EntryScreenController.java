package com.yagodaoud.comandae.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntryScreenController {

    @FXML
    public void goToMenu() {
        showMessage("Navigating to Menu Screen...");
        // TODO: Implement navigation to Menu screen using Spring
    }

    @FXML
    public void goToOrderManagement() {
        showMessage("Navigating to Order Management Screen...");
        // TODO: Implement navigation to Order Management screen using Spring
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Navigation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}