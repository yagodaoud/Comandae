package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class SidebarController {

    private final StageManager stageManager;

    @FXML
    private HBox dashboardButton;

    @FXML
    private HBox menuButton;

    @FXML
    private HBox orderManagementButton;

    @FXML
    public HBox productsButton;

    @FXML
    private HBox financialButton;

    @FXML
    private HBox settingsButton;

    @FXML
    private HBox signOutButton;

    private NavigationScreen currentScreen = NavigationScreen.DASHBOARD;


    public void setSelectedScreen(NavigationScreen screen) {
        currentScreen = screen;
        clearSelection();

        switch (screen) {
            case DASHBOARD -> dashboardButton.getStyleClass().add("selected");
            case MENU -> menuButton.getStyleClass().add("selected");
            case ORDER_MANAGEMENT -> orderManagementButton.getStyleClass().add("selected");
            case PRODUCT -> productsButton.getStyleClass().add("selected");
            case FINANCIAL -> financialButton.getStyleClass().add("selected");
            case SETTINGS -> settingsButton.getStyleClass().add("selected");
        }
    }

    private void clearSelection() {
        dashboardButton.getStyleClass().remove("selected");
        menuButton.getStyleClass().remove("selected");
        orderManagementButton.getStyleClass().remove("selected");
        financialButton.getStyleClass().remove("selected");
        settingsButton.getStyleClass().remove("selected");
    }

    @Autowired
    public SidebarController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void handleDashboardButton() {
        if (currentScreen != NavigationScreen.DASHBOARD) {
            stageManager.switchScene("view/MainScreen.fxml", "Main", false);
        }
    }

    @FXML
    private void handleSignOutButton() {
        stageManager.switchScene("view/EntryScreen.fxml", "Entry", false);
    }

    @FXML
    private void handleMenuButton() {
        if (currentScreen != NavigationScreen.MENU) {
            stageManager.switchScene("view/MenuScreen.fxml", "Menu", false);
        }
    }

    @FXML
    private void handleFinancialButton() {
        if (currentScreen != NavigationScreen.FINANCIAL) {
            stageManager.switchScene("view/FinancialScreen.fxml", "Financial", false);
        }
    }

    @FXML
    private void handleOrderManagementButton() {
        if (currentScreen != NavigationScreen.ORDER_MANAGEMENT) {
            stageManager.switchScene("view/OrderManagementScreen.fxml", "Order Management", false);
        }
    }

    @FXML
    private void handleProductsButton() {
        if (currentScreen != NavigationScreen.PRODUCT) {
            stageManager.switchScene("view/Products.fxml", "Products", false);
        }
    }


}
