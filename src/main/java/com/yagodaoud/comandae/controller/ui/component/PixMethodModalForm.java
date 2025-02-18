package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.PixDTO;
import com.yagodaoud.comandae.model.Pix;
import com.yagodaoud.comandae.model.PixType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class PixMethodModalForm extends VBox {
    private final TextField keyField;
    private final TextField companyNameField;
    private final ComboBox<PixType> typeComboBox;
    private final Button saveButton;
    private final VBox pixRecordsBox;
    private final List<Pix> existingPixKeys;
    private final Consumer<Pix> onDeletePixKey;
    private static final String DEFAULT_CITY = "Brasil";

    public PixMethodModalForm(
            List<PixType> pixTypes,
            List<Pix> existingPixKeys,
            Runnable onCancel,
            java.util.function.Consumer<PixDTO> onSave,
            Consumer<Pix> onDeletePixKey
    ) {
        this.existingPixKeys = existingPixKeys;
        this.onDeletePixKey = onDeletePixKey;

        getStyleClass().add("modal-form");
        setSpacing(15);
        setPadding(new Insets(20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("PIX Key Registration");
        title.getStyleClass().add("modal-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label closeButton = new Label("\uE5CD");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnMouseClicked(e -> onCancel.run());

        headerBox.getChildren().addAll(title, spacer, closeButton);

        Label companyLabel = new Label("Company Name:");
        companyLabel.getStyleClass().add("form-label");
        companyNameField = new TextField();
        companyNameField.setPromptText("Enter Company Name");

        Label keyLabel = new Label("Key:");
        keyLabel.getStyleClass().add("form-label");
        keyField = new TextField();
        keyField.setPromptText("Enter PIX Key");

        Label typeLabel = new Label("Type:");
        typeLabel.getStyleClass().add("form-label");
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(pixTypes);
        typeComboBox.setPromptText("Select PIX Type");
        typeComboBox.getStyleClass().addAll("combo-box", "form-input");

        saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button", "primary-button");
        saveButton.setOnAction(e -> {
            String key = keyField.getText().trim();
            String companyName = companyNameField.getText().trim();
            PixType type = typeComboBox.getValue();

            if (!key.isEmpty() && !companyName.isEmpty() && type != null) {
                PixDTO pixDTO = new PixDTO();
                pixDTO.setKey(key);
                pixDTO.setType(type);
                pixDTO.setCompanyName(companyName);
                pixDTO.setCity(DEFAULT_CITY);
                pixDTO.setIsActive(true);
                pixDTO.setCreatedAt(LocalDateTime.now());

                onSave.accept(pixDTO);

                Pix newPixKey = new Pix();
                newPixKey.setKey(key);
                newPixKey.setType(type);
                existingPixKeys.add(newPixKey);

                populatePixRecords(existingPixKeys);
            }
        });
        saveButton.setDisable(true);

        keyField.textProperty().addListener((obs, old, newValue) -> validateInput());
        companyNameField.textProperty().addListener((obs, old, newValue) -> validateInput());
        typeComboBox.valueProperty().addListener((obs, old, newValue) -> validateInput());

        Text recordsTitle = new Text("Keys");
        recordsTitle.getStyleClass().add("modal-subtitle");

        pixRecordsBox = new VBox(10);
        pixRecordsBox.getStyleClass().add("pix-records");
        populatePixRecords(existingPixKeys);

        getChildren().addAll(
                headerBox,
                companyLabel, companyNameField,
                keyLabel, keyField,
                typeLabel, typeComboBox,
                saveButton,
                recordsTitle,
                pixRecordsBox
        );
    }

    private void validateInput() {
        boolean isValid = !keyField.getText().trim().isEmpty()
                && !companyNameField.getText().trim().isEmpty()
                && typeComboBox.getValue() != null;
        saveButton.setDisable(!isValid);
    }

    private void populatePixRecords(List<Pix> pixKeys) {
        pixRecordsBox.getChildren().clear();

        for (Pix pixKey : pixKeys) {
            HBox pixRecord = new HBox(10);
            pixRecord.getStyleClass().add("pix-record");
            pixRecord.setAlignment(Pos.CENTER_LEFT);

            Label typeLabel = new Label(pixKey.getType().toString());
            typeLabel.getStyleClass().add("pix-record-label");

            Label keyLabel = new Label(formatPixKey(pixKey.getKey()));
            keyLabel.getStyleClass().add("pix-record-key");
            HBox.setHgrow(keyLabel, Priority.ALWAYS);

            Label deleteLabel = new Label("\uE872");
            deleteLabel.getStyleClass().add("delete-button");
            deleteLabel.setOnMouseClicked(e -> {
                pixRecordsBox.getChildren().remove(pixRecord);
                existingPixKeys.remove(pixKey);
                onDeletePixKey.accept(pixKey);
            });

            pixRecord.getChildren().addAll(typeLabel, keyLabel, deleteLabel);
            pixRecordsBox.getChildren().add(pixRecord);
        }
    }

    private String formatPixKey(String pixKey) {
        if (pixKey.length() < 20) return pixKey;
        return pixKey.substring(0, 20) + "..." + pixKey.substring(pixKey.length() - 4);
    }
}