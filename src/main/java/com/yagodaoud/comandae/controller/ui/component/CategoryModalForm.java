package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.menu.MenuCategoryDTO;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class CategoryModalForm extends GridPane {
    private final TextField nameField;
    private final Spinner<Integer> displayOrderSpinner;
    private final Button saveButton;
    private final Button cancelButton;

    public CategoryModalForm(Runnable onCancel, java.util.function.Consumer<MenuCategoryDTO> onSave) {
        setHgap(10);
        setVgap(15);
        getStyleClass().add("modal-form");

        Text title = new Text("Add Category");
        title.getStyleClass().add("modal-title");
        add(title, 0, 0, 2, 1);

        nameField = new TextField();
        nameField.setPromptText("Category name");
        addFormRow("Name:", nameField, 1);

        displayOrderSpinner = new Spinner<>(0, 100, 1);
        displayOrderSpinner.setEditable(true);
        displayOrderSpinner.setPrefWidth(100);
        addFormRow("Display Order:", displayOrderSpinner, 2);

        saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button", "primary-button");
        saveButton.setOnAction(e -> {
            MenuCategoryDTO category = new MenuCategoryDTO();
            category.setName(nameField.getText());
            category.setDisplayOrder(displayOrderSpinner.getValue());
            onSave.accept(category);
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "secondary-button");
        cancelButton.setOnAction(e -> onCancel.run());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 3, 2, 1);

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
