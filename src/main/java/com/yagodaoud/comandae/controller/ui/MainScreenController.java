package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
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
    private Text appTitle;

    @FXML
    private BarChart<String, Number> salesChart;

    private void setupSalesChart() {
        salesChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales Status");

        series.getData().add(new XYChart.Data<>("Successful", 65));
        series.getData().add(new XYChart.Data<>("Prepared", 45));
        series.getData().add(new XYChart.Data<>("Returned", 15));
        series.getData().add(new XYChart.Data<>("Shipped out", 55));

        salesChart.getData().add(series);

        salesChart.setLegendVisible(false);
        salesChart.setAnimated(false);

        // Minimalist style adjustments
        salesChart.setCategoryGap(50);
        salesChart.setBarGap(16);

        salesChart.setStyle("-fx-background-color: transparent;");
        salesChart.getXAxis().setTickLabelsVisible(true);
        salesChart.getYAxis().setTickLabelsVisible(true);
        salesChart.getYAxis().setOpacity(0); // Make the Y-axis line invisible
        

    }

    @FXML
    public void initialize() {
        appTitle.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
//                String cssPath = getClass().getResource("/css/mainScreen.css").toExternalForm();
//                newScene.getStylesheets().add(cssPath);
                setupSalesChart();
            }
        });
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

    @FXML
    private void handleSignOutButton() {
        stageManager.switchScene("view/EntryScreen.fxml", "Entry Screen");
    }

    @FXML
    private void handleMenuButton() {
        stageManager.switchScene("view/MenuScreen.fxml", "Menu Screen");
    }
}