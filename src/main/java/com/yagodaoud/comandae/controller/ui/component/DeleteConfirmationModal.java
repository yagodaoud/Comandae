package com.yagodaoud.comandae.controller.ui.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DeleteConfirmationModal extends GridPane {
    private final Button confirmButton;
    private final Button cancelButton;

    public DeleteConfirmationModal(String message,
                             Runnable onCancel,
                             Runnable onConfirm) {
        setHgap(10);
        setVgap(5);
        getStyleClass().add("modal-form-confirmation");

        Text messageText = new Text(message);
        messageText.getStyleClass().add("confirmation-message");
        add(messageText, 0, 0, 2, 1);

        confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().addAll("button", "danger-button");
        confirmButton.setOnAction(e -> onConfirm.run());

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "secondary-button");
        cancelButton.setOnAction(e -> onCancel.run());

        confirmButton.setMinWidth(100);
        cancelButton.setMinWidth(100);

        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 1, 2, 1);

        setPrefHeight(10);

    }
}
