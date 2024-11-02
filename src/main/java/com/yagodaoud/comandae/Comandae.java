package com.yagodaoud.comandae;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Comandae extends Application {

    private ConfigurableApplicationContext context;
    private StageManager stageManager;

    @Override
    public void init() throws IOException {
        context = SpringApplication.run(Comandae.class);
    }

    @Override
    public void start(Stage primaryStage) {
        stageManager = context.getBean(StageManager.class);
        stageManager.setPrimaryStage(primaryStage);

        stageManager.switchScene("view/MainMenu.fxml", "Comandaê");

        primaryStage.setMaximized(true);
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
