package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.controller.ui.component.*;
import com.yagodaoud.comandae.dto.CategoryDTO;
import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.service.CategoryService;
import com.yagodaoud.comandae.service.ProductService;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import java.util.Comparator;
import java.util.List;

@Component
@Lazy
public class ProductsScreenController {
    private final StageManager stageManager;

    @FXML
    public ScrollPane categoriesFlowPane;

    @FXML
    public Button addCategoryButton;

    private ObservableList<Category> categories;
    @FXML
    private VBox categoriesList;

    @FXML
    public ScrollPane productsFlowPane;

    @FXML
    public Button addProductButton;

    @FXML
    private VBox productsList;

    private ObservableList<Product> products;

    @FXML
    private StackPane modalContainer;
    private ModalContainer modal;

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    public ProductsScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void initialize() {
        sidebarController.setSelectedScreen(NavigationScreen.PRODUCT);

        loadCategories();
        loadProducts();

        modal = new ModalContainer();
        modalContainer.getChildren().add(modal);

        modalContainer.setPickOnBounds(false);
        modalContainer.toFront();
    }

    private void loadCategories() {
        List<Category> categoryList = categoryService.getAll(false);

        categoryList.sort(Comparator.comparingLong(Category::getId));

        categories = FXCollections.observableArrayList(categoryList);
        populateCategories();
    }

    private void populateCategories() {
        for (Category category : categories) {

            ProductCategoryCard categoryCard = new ProductCategoryCard(category, this::showEditCategoryDialog);

            categoriesList.getChildren().add(categoryCard);
        }
    }

    private void refreshCategories(Category category) {

        ProductCategoryCard categoryCard = new ProductCategoryCard(category, this::showEditCategoryDialog);

        categoriesList.getChildren().add(categoryCard);

    }

    private void loadProducts() {
        List<Product> productList = productService.getAll(false);

        productList.sort(Comparator.comparingLong(Product::getId));

        products = FXCollections.observableArrayList(productList);
        populateProducts();
    }

    private void populateProducts() {
        for (Product product : products) {

            ProductCard productCard = new ProductCard(product, this::showEditProductDialog);

            productsList.getChildren().add(productCard);
        }
    }

    private void refreshProducts(Product product) {

        ProductCard productCard = new ProductCard(product, this::showEditProductDialog);

        productsList.getChildren().add(productCard);
    }


    @FXML
    private void addCategory(ActionEvent event) {
        ProductCategoryModalForm form = new ProductCategoryModalForm(
                () -> modal.hide(),
                categoryDTO -> {
                    Category savedCategory = categoryService.create(categoryDTO);
                    categories.add(savedCategory);
                    refreshCategories(savedCategory);
                    modal.hide();
                }
        );

        modal.show(form);
    }

    @FXML
    private void addProduct(ActionEvent event) {
        ProductModalForm form = new ProductModalForm(
                categories,
                () -> modal.hide(),
                productDTO -> {
                    Product savedProduct = productService.create(productDTO);
                    products.add(savedProduct);
                    refreshProducts(savedProduct);
                    modal.hide();
                }
        );

        modal.show(form);
    }

    @FXML
    private void showEditCategoryDialog(Category category) {
        ProductCategoryModalForm form = new ProductCategoryModalForm(
                () -> modal.hide(),
                categoryDTO -> {
                    Category updatedCategory = categoryService.update(categoryDTO.getId(), categoryDTO);
                    int index = categories.indexOf(category);
                    if (index >= 0) {
                        categories.set(index, updatedCategory);
                    }
                    refreshCategories(updatedCategory);
                    modal.hide();
                },
                category
        );

        modal.show(form);
    }

    @FXML
    private void showEditProductDialog(Product product) {
        ProductModalForm form = new ProductModalForm(
                categories,
                () -> modal.hide(),
                productDTO -> {
                    Product updatedProduct = productService.update(productDTO.getId(), productDTO);
                    int index = products.indexOf(product);
                    if (index >= 0) {
                        products.set(index, updatedProduct);
                    }
                    refreshProducts(updatedProduct);
                    modal.hide();
                },
                product
        );

        modal.show(form);
    }

}