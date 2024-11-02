package com.yagodaoud.comandae;

import com.yagodaoud.comandae.controller.ui.JavaFXController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Comandae extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = SpringApplication.run(Comandae.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        context.getBean(JavaFXController.class).start(primaryStage);
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
