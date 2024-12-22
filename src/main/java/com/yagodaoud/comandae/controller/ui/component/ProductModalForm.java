package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.Product;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import java.math.BigDecimal;

public class ProductModalForm extends GridPane {
    private final TextField nameField;
    private final TextField priceField;
    private final ComboBox<Category> categoryComboBox;
    private final Button saveButton;
    private final Button cancelButton;
    private Long editingId;

    public ProductModalForm(java.util.List<Category> categories,
                            Runnable onCancel,
                            java.util.function.Consumer<ProductDTO> onSave,
                            Product existingProduct) {
        setHgap(10);
        setVgap(15);
        getStyleClass().add("modal-form");

        Text title = new Text(existingProduct == null ? "Add Product" : "Edit Product");
        title.getStyleClass().add("modal-title");
        add(title, 0, 0, 2, 1);

        nameField = new TextField();
        nameField.setPromptText("Product name");
        addFormRow("Name:", nameField, 1);

        priceField = new TextField();
        priceField.setPromptText("0.00");
        addFormRow("Price:", priceField, 2);

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(javafx.collections.FXCollections.observableArrayList(categories));
        categoryComboBox.setPromptText("Select category");
        categoryComboBox.setCellFactory(lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty ? "" : category.getName());
            }
        });
        categoryComboBox.setButtonCell(categoryComboBox.getCellFactory().call(null));
        addFormRow("Category:", categoryComboBox, 3);

        if (existingProduct != null) {
            editingId = existingProduct.getId();
            nameField.setText(existingProduct.getName());
            priceField.setText(existingProduct.getPrice().toString());
            Category matchingCategory = categories.stream()
                    .filter(c -> c.getId().equals(existingProduct.getCategory().getId()))
                    .findFirst()
                    .orElse(null);
            categoryComboBox.setValue(matchingCategory);
        }

        saveButton = new Button(existingProduct == null ? "Save" : "Update");
        saveButton.getStyleClass().addAll("button", "primary-button");
        saveButton.setOnAction(e -> {
            try {
                ProductDTO product = new ProductDTO();
                product.setName(nameField.getText());
                product.setPrice(new BigDecimal(priceField.getText()));
                product.setCategory(categoryComboBox.getValue());
                if (editingId != null) {
                    product.setId(editingId);
                }
                onSave.accept(product);
            } catch (NumberFormatException ignored) {
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "secondary-button");
        cancelButton.setOnAction(e -> onCancel.run());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 4, 2, 1);

        setupValidation();
    }

    public ProductModalForm(java.util.List<Category> categories,
                            Runnable onCancel,
                            java.util.function.Consumer<ProductDTO> onSave) {
        this(categories, onCancel, onSave, null);
    }

    private void setupValidation() {
        saveButton.setDisable(true);

        javafx.beans.binding.BooleanBinding isValid = nameField.textProperty().isEmpty()
                .or(priceField.textProperty().isEmpty())
                .or(categoryComboBox.valueProperty().isNull());

        saveButton.disableProperty().bind(isValid);

        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                priceField.setText(oldVal);
            }
        });
    }

    private void addFormRow(String label, Control field, int row) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("form-label");
        add(labelNode, 0, row);
        add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }
}