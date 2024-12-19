package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.CategoryService;
import com.yagodaoud.comandae.service.ProductService;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class ProductsScreenController {
    private final StageManager stageManager;

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane categoryScrollPane;
    @FXML
    private FlowPane categoryFlowPane;

    @FXML
    private ScrollPane productScrollPane;
    @FXML
    private FlowPane productFlowPane;

    @FXML
    private Button addProductButton;
    @FXML
    private Button addCategoryButton;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    public ProductsScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void initialize() {
        sidebarController.setSelectedScreen(NavigationScreen.PRODUCT);
//        loadCategories();
        loadProducts();
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAll(false);
        categoryFlowPane.getChildren().clear();
        for (Category category : categories) {
            VBox categoryBox = new VBox();
            categoryBox.getStyleClass().add("category-box");

            // Add category image, name, and item count
            // ...

            categoryFlowPane.getChildren().add(categoryBox);
        }
    }

    private void loadProducts() {
        List<Product> products = productService.getAll(false);
        productFlowPane.getChildren().clear();
        for (Product product : products) {
            VBox productBox = new VBox();
            productBox.getStyleClass().add("product-box");

            // Add product image, name, and price
            // ...

            productFlowPane.getChildren().add(productBox);
        }
    }

    @FXML
    private void handleSearchFieldAction() {
        // Perform search logic
    }

    @FXML
    private void handleAddProduct() {
        // Show add product dialog
    }

    @FXML
    private void handleAddCategory() {
        // Show add category dialog
    }
}