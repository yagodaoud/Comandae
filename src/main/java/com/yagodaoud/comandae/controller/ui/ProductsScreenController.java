package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.controller.ui.component.*;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.CategoryService;
import com.yagodaoud.comandae.service.ProductService;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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

    @FXML
    public TextField searchField;

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

    private ObservableList<Product> allProducts;
    private ObservableList<Category> allCategories;

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

        setupSearch();
    }

    private void loadCategories() {
        List<Category> categoryList = categoryService.getAll(false);
        categoryList.sort(Comparator.comparingLong(Category::getId));

        allCategories = FXCollections.observableArrayList(categoryList);
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
        String currentSearch = searchField.getText().toLowerCase().trim();
        if (category.getName().toLowerCase().contains(currentSearch)) {
            ProductCategoryCard categoryCard = new ProductCategoryCard(category, this::showEditCategoryDialog);
            categoriesList.getChildren().add(categoryCard);
        }
    }

    private void loadProducts() {
        List<Product> productList = productService.getAll(false);
        productList.sort(Comparator.comparingLong(Product::getId));

        allProducts = FXCollections.observableArrayList(productList);
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
        String currentSearch = searchField.getText().toLowerCase().trim();
        if (product.getName().toLowerCase().contains(currentSearch) ||
                (product.getCategory() != null &&
                    product.getCategory().getName().toLowerCase().contains(currentSearch))) {

            ProductCard existingProductCard = findProductCard(product);
            if (existingProductCard != null) {
                existingProductCard.updateProduct(product);
                return;
            }

            ProductCard productCard = new ProductCard(product, this::showEditProductDialog);
            productsList.getChildren().add(productCard);
        }
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

    private ProductCard findProductCard(Product product) {
        for (Node node : productsList.getChildren()) {
            if (node instanceof ProductCard productCard) {
                if (productCard.getProduct().getId().equals(product.getId())) {
                    return productCard;
                }
            }
        }
        return null;
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.toLowerCase().trim();
            filterItems(searchTerm);
        });
    }

    private void filterItems(String searchTerm) {
        categoriesList.getChildren().clear();
        productsList.getChildren().clear();

        if (allCategories != null) {
            allCategories.stream()
                    .filter(category -> category.getName().toLowerCase().contains(searchTerm))
                    .forEach(category -> {
                        ProductCategoryCard categoryCard = new ProductCategoryCard(category, this::showEditCategoryDialog);
                        categoriesList.getChildren().add(categoryCard);
                    });
        }

        if (allProducts != null) {
            allProducts.stream()
                    .filter(product ->
                            product.getName().toLowerCase().contains(searchTerm) ||
                                    (product.getCategory() != null &&
                                            product.getCategory().getName().toLowerCase().contains(searchTerm)))
                    .forEach(product -> {
                        ProductCard productCard = new ProductCard(product, this::showEditProductDialog);
                        productsList.getChildren().add(productCard);
                    });
        }
    }

}