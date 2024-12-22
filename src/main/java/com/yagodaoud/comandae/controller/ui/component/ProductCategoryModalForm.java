package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.CategoryDTO;
import com.yagodaoud.comandae.model.Category;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;

public class ProductCategoryModalForm extends GridPane {
    private final TextField nameField;
    private final TextField imagePathField;
    private final Button browseButton;
    private final Button saveButton;
    private final Button cancelButton;
    private Long editingId;

    public ProductCategoryModalForm(Runnable onCancel,
                             java.util.function.Consumer<CategoryDTO> onSave,
                             Category existingCategory) {
        setHgap(10);
        setVgap(15);
        getStyleClass().add("modal-form");

        Text title = new Text(existingCategory == null ? "Add Category" : "Edit Category");
        title.getStyleClass().add("modal-title");
        add(title, 0, 0, 2, 1);

        nameField = new TextField();
        nameField.setPromptText("Category name");
        addFormRow("Name:", nameField, 1);

        imagePathField = new TextField();
        imagePathField.setPromptText("Image path");
        imagePathField.setEditable(false);

        browseButton = new Button("Browse");
        browseButton.getStyleClass().addAll("button", "secondary-button");
        browseButton.setOnAction(e -> handleImageBrowse());

        if (existingCategory != null) {
            editingId = existingCategory.getId();
            nameField.setText(existingCategory.getName());
            imagePathField.setText(existingCategory.getImage());
        }

        saveButton = new Button(existingCategory == null ? "Save" : "Update");
        saveButton.getStyleClass().addAll("button", "add-button");
        saveButton.setOnAction(e -> {
            CategoryDTO category = new CategoryDTO();
            category.setName(nameField.getText());
            category.setImage(imagePathField.getText());
            if (editingId != null) {
                category.setId(editingId);
            }
            onSave.accept(category);
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "cancel-button");
        cancelButton.setOnAction(e -> onCancel.run());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 3, 2, 1);

        setupValidation();
    }

    public ProductCategoryModalForm(Runnable onCancel, java.util.function.Consumer<CategoryDTO> onSave) {
        this(onCancel, onSave, null);
    }

    private void handleImageBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void setupValidation() {
        nameField.textProperty().addListener((obs, old, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        saveButton.setDisable(true);
    }

    private void addFormRow(String label, Control field, int row) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("form-label");
        add(labelNode, 0, row);
        add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }
}