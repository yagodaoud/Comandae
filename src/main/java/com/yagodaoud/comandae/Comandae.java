package com.yagodaoud.comandae;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.application.Application;
import javafx.scene.image.Image;
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

        primaryStage.getIcons().add(new Image("https://brandslogos.com/wp-content/uploads/images/large/java-logo-1.png"));

        stageManager.setPrimaryStage(primaryStage);

        stageManager.switchScene("view/EntryScreen.fxml", "Comandaê");

        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
