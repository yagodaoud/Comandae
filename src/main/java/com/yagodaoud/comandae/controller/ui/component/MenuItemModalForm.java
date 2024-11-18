package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.menu.MenuItemDTO;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class MenuItemModalForm extends GridPane {
    private final TextField nameField;
    private final TextField emojiField;
    private final TextField priceField;
    private final TextArea descriptionArea;
    private final ComboBox<MenuCategory> categoryComboBox;
    private final Button saveButton;
    private final Button cancelButton;

    public MenuItemModalForm(java.util.List<MenuCategory> categories,
                             Runnable onCancel,
                             java.util.function.Consumer<MenuItemDTO> onSave) {
        setHgap(10);
        setVgap(15);
        getStyleClass().add("modal-form");

        // Title
        Text title = new Text("Add Menu Item");
        title.getStyleClass().add("modal-title");
        add(title, 0, 0, 2, 1);

        // Form fields
        nameField = new TextField();
        nameField.setPromptText("Item name");
        addFormRow("Name:", nameField, 1);

        emojiField = new TextField();
        emojiField.setPromptText("Item emoji");
        addFormRow("Emoji:", emojiField, 2);

        priceField = new TextField();
        priceField.setPromptText("0.00");
        addFormRow("Price:", priceField, 3);

        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(categories);
        categoryComboBox.setPromptText("Select category");
        setupCategoryComboBox();
        addFormRow("Category:", categoryComboBox, 4);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Item description");
        descriptionArea.setPrefRowCount(3);
        addFormRow("Description:", descriptionArea, 5);

        // Buttons
        saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button", "primary-button");
        saveButton.setOnAction(e -> {
            MenuItemDTO item = new MenuItemDTO();
            item.setName(nameField.getText());
            item.setEmoji(emojiField.getText());
            try {
//                item.setPrice(Double.parseDouble(priceField.getText()));
            } catch (NumberFormatException ex) {
//                item.setPrice(0.0);
            }
//            item.setDescription(descriptionArea.getText());
            item.setCategoryId(categoryComboBox.getValue().getId());
            onSave.accept(item);
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "secondary-button");
        cancelButton.setOnAction(e -> onCancel.run());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 6, 2, 1);

        // Validation
        setupValidation();
    }

    private void setupCategoryComboBox() {
        categoryComboBox.setCellFactory(lv -> new ListCell<MenuCategory>() {
            @Override
            protected void updateItem(MenuCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        categoryComboBox.setButtonCell(new ListCell<MenuCategory>() {
            @Override
            protected void updateItem(MenuCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
    }

    private void setupValidation() {
        saveButton.setDisable(true);

        // Enable save button only when required fields are filled
        nameField.textProperty().addListener((obs, old, newValue) -> validateInput());
        priceField.textProperty().addListener((obs, old, newValue) -> validateInput());
        categoryComboBox.valueProperty().addListener((obs, old, newValue) -> validateInput());
    }

    private void validateInput() {
        boolean isValid = !nameField.getText().trim().isEmpty()
                && !priceField.getText().trim().isEmpty()
                && categoryComboBox.getValue() != null
                && isPriceValid(priceField.getText());
        saveButton.setDisable(!isValid);
    }

    private boolean isPriceValid(String price) {
        try {
            double value = Double.parseDouble(price);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void addFormRow(String label, Control field, int row) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("form-label");
        add(labelNode, 0, row);
        add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }
}