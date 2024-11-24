package com.yagodaoud.comandae.controller.ui.component;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GeneratedMenuModal extends VBox {
    private final String menuContent;
    private final Runnable onClose;
    private TextArea menuArea;

    public GeneratedMenuModal(
            String menuContent,
            Runnable onCloseHandler
    ) {
        this.menuContent = menuContent;
        this.onClose = onCloseHandler;

        setupUI();
    }

    private void setupUI() {
        getStyleClass().add("modal-form");
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(500);
        setMaxHeight(600);

        Text title = new Text("Generated Menu");
        title.getStyleClass().add("modal-title");

        setupMenuArea();
        setupButtons();

        getChildren().addAll(title, menuArea, setupButtons());
    }

    private void setupMenuArea() {
        menuArea = new TextArea(menuContent);
        menuArea.setEditable(false);
        menuArea.setWrapText(true);
        menuArea.setPrefRowCount(40);
        menuArea.getStyleClass().add("menu-text-area");
    }

    private HBox setupButtons() {
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button copyButton = new Button("Copy to Clipboard");
        copyButton.getStyleClass().add("primary-button");
        copyButton.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(menuContent);
            Clipboard.getSystemClipboard().setContent(content);

            copyButton.setText("Copied!");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> copyButton.setText("Copy to Clipboard"));
            pause.play();
        });

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("secondary-button");
        closeButton.setOnAction(e -> onClose.run());

        buttonBox.getChildren().addAll(copyButton, closeButton);
        return buttonBox;
    }

    public String getMenuContent() {
        return menuContent;
    }

    public TextArea getMenuArea() {
        return menuArea;
    }
}
