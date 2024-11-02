package com.yagodaoud.comandae.controller.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JavaFXController {

    @Autowired
    private ApplicationContext context;


    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EntryScreen.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Daily Menu Management");
        primaryStage.show();
    }
}
