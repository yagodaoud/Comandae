package com.yagodaoud.comandae.controller.ui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuSettingsModal extends VBox {
    private final Runnable onClose;
    private final Runnable onSave;
    private TextArea headerTextArea;
    private TextArea footerTextArea;

    public MenuSettingsModal(
            String currentHeader,
            String currentFooter,
            Runnable onCloseHandler,
            Runnable onSaveHandler
    ) {
        this.onClose = onCloseHandler;
        this.onSave = onSaveHandler;

        setupUI(currentHeader, currentFooter);
    }

    private void setupUI(String currentHeader, String currentFooter) {
        getStyleClass().add("modal-form");
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(500);

        Text title = new Text("Menu Settings");
        title.getStyleClass().add("modal-title");

        Label headerLabel = new Label("Menu Header");
        headerLabel.getStyleClass().add("form-label");

        headerTextArea = new TextArea(currentHeader);
        headerTextArea.setPromptText("Enter menu header text...");
        headerTextArea.setPrefRowCount(5);
        headerTextArea.setWrapText(true);
        headerTextArea.getStyleClass().add("menu-settings-textarea");

        Label footerLabel = new Label("Menu Footer");
        footerLabel.getStyleClass().add("form-label");

        footerTextArea = new TextArea(currentFooter);
        footerTextArea.setPromptText("Enter menu footer text...");
        footerTextArea.setPrefRowCount(5);
        footerTextArea.setWrapText(true);
        footerTextArea.getStyleClass().add("menu-settings-textarea");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("secondary-button");
        cancelButton.setOnAction(e -> onClose.run());

        Button saveButton = new Button("Save Settings");
        saveButton.getStyleClass().add("primary-button");
        saveButton.setOnAction(e -> onSave.run());

        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, saveButton);

        getChildren().addAll(
                title,
                headerLabel,
                headerTextArea,
                footerLabel,
                footerTextArea,
                buttonBox
        );

        setPadding(new Insets(20));
    }

    public String getHeaderText() {
        return headerTextArea.getText();
    }

    public String getFooterText() {
        return footerTextArea.getText();
    }
}
