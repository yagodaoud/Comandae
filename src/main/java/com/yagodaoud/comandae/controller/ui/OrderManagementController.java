package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderManagementController {

    private final StageManager stageManager;

    @Autowired
    public OrderManagementController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private TableView<?> ordersTable;

    @FXML
    private TableColumn<?, ?> orderIdColumn;

    @FXML
    private TableColumn<?, ?> orderDateColumn;

    @FXML
    private TableColumn<?, ?> orderStatusColumn;

    @FXML
    private TableColumn<?, ?> orderTotalColumn;

    @FXML
    private TextField orderIdField;

    @FXML
    private TextField orderDateField;

    @FXML
    private TextField orderStatusField;

    @FXML
    private TextField orderTotalField;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button editOrderButton;

    @FXML
    private Button deleteOrderButton;

    @FXML
    private Button refreshButton;

    @FXML
    public void handleAddOrder() {
        showAlert("Add Order", "Add order functionality is not yet implemented.");
    }

    @FXML
    public void handleEditOrder() {
        showAlert("Edit Order", "Edit order functionality is not yet implemented.");
    }

    @FXML
    public void handleDeleteOrder() {
        showAlert("Delete Order", "Delete order functionality is not yet implemented.");
    }

    @FXML
    public void handleRefresh() {
        showAlert("Refresh Orders", "Refresh functionality is not yet implemented.");
    }

    @FXML
    public void goToEntryScreen() {
        stageManager.switchScene("view/MainScreen.fxml", "Comandaê");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}