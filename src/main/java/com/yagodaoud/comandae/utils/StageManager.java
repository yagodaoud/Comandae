package com.yagodaoud.comandae.utils;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageManager {

    private final ApplicationContext context;
    private Stage primaryStage;

    @Autowired
    public StageManager(ApplicationContext context) {
        this.context = context;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchScene(String fxmlPath, String title) {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/MaterialIcons-Regular.ttf"), 16);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root));

            fadeIn.play();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Could not load view: " + title);
        }
    }

    private void showError(String message) {
        System.err.println(message);
    }
}
