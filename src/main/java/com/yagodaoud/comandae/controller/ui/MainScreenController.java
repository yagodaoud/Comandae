package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.ProductService;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class MainScreenController {

    private final StageManager stageManager;

    @FXML
    public VBox inventoryStatus;

    @Autowired
    public MainScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @FXML
    private BarChart<String, Number> salesChart;

    @Autowired
    private ProductService productService;


    @FXML
    public void initialize() {
        setupSalesChart();
        sidebarController.setSelectedScreen(NavigationScreen.DASHBOARD);

        populateLowStockItems();
    }

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

        salesChart.setCategoryGap(50);
        salesChart.setBarGap(16);

        salesChart.setStyle("-fx-background-color: transparent;");
        salesChart.getXAxis().setTickLabelsVisible(true);
        salesChart.getYAxis().setTickLabelsVisible(true);
        salesChart.getYAxis().setOpacity(0);
    }

    private void populateLowStockItems() {
        int lowStockThreshold = 10;
        List<Product> lowStockProducts = productService.findLowStockProducts(lowStockThreshold);
        inventoryStatus.getChildren().clear();

        inventoryStatus.setSpacing(10);

        HBox headerHBox = new HBox();
        headerHBox.setAlignment(Pos.CENTER_LEFT);

        Text headerText = new Text("Low Stock Items");
        headerText.getStyleClass().add("section-sub-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerHBox.getChildren().addAll(headerText, spacer);
        inventoryStatus.getChildren().add(headerHBox);

        if (lowStockProducts.isEmpty()) {
            HBox itemHBox = new HBox();
            itemHBox.setSpacing(10);
            itemHBox.setAlignment(Pos.CENTER_LEFT);

            Text nameText = new Text("No low stock items to show.");
            nameText.getStyleClass().add("inventory-item-name");

            itemHBox.getChildren().addAll(nameText);
            inventoryStatus.getChildren().add(itemHBox);

            return;
        }

        for (Product product : lowStockProducts) {
            HBox itemHBox = new HBox();
            itemHBox.setSpacing(10);
            itemHBox.setAlignment(Pos.CENTER_LEFT);

            Text nameText = new Text(product.getName());
            nameText.getStyleClass().add("inventory-item-name");

            Region itemSpacer = new Region();
            HBox.setHgrow(itemSpacer, Priority.ALWAYS);

            Text quantityText = new Text(String.valueOf(product.getStockQuantity()));
            quantityText.getStyleClass().add("inventory-item-quantity");

            itemHBox.getChildren().addAll(nameText, itemSpacer, quantityText);
            inventoryStatus.getChildren().add(itemHBox);
        }
    }
}