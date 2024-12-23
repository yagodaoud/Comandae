package com.yagodaoud.comandae.controller.ui.component;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ModalContainer extends StackPane {
    private final StackPane modalOverlay;
    private final VBox modalContent;

    public ModalContainer() {
        setPickOnBounds(false);

        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        modalOverlay.setVisible(false);
        modalOverlay.setPickOnBounds(true);

        modalContent = new VBox();
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");
        modalContent.setMaxWidth(400);
        modalContent.setMaxHeight(600);
        modalContent.setSpacing(15);
        modalContent.setPadding(new Insets(20));
        modalContent.setPickOnBounds(true);

        modalOverlay.getChildren().add(modalContent);
        getChildren().add(modalOverlay);

        StackPane.setAlignment(modalContent, Pos.CENTER);

        modalOverlay.setOnMouseClicked(event -> {
            if (event.getTarget() == modalOverlay) {
                hide();
                event.consume();
            }
        });
    }

    public void show(Node content) {
        modalContent.getChildren().setAll(content);
        modalOverlay.setVisible(true);
        setPickOnBounds(true);

        toFront();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), modalOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void hide() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), modalOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            modalContent.getChildren().clear();
            setPickOnBounds(false);
        });
        fadeOut.play();
    }
}