package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MainScreenController {

    private final StageManager stageManager;

    @Autowired
    public MainScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

//    @FXML
//    private SVGPath notificationIcon;
//
//    @FXML
//    private SVGPath profileIcon;
//
//    @FXML
//    private SVGPath settingsIcon;

    @FXML
    private Text appTitle;

    @FXML
    private Button exploreButton;

    @FXML
    private Pane salesChart;

    @FXML
    public void initialize() {

//        salesChart.setImage(new Image("https://i.pinimg.com/originals/0d/bb/dd/0dbbdd2944e06b540a3341c8d6a801e4.jpg"));

        appTitle.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                String cssPath = getClass().getResource("/css/mainScreen.css").toExternalForm();
                newScene.getStylesheets().add(cssPath);

//                notificationIcon.setFill(Color.valueOf("#4A4A4A"));
//                profileIcon.setFill(Color.valueOf("#4A4A4A"));
//                settingsIcon.setFill(Color.valueOf("#4A4A4A"));

//                notificationIcon.setOnMouseClicked(event -> handleNotificationIconClick());
//                profileIcon.setOnMouseClicked(event -> handleProfileIconClick());
//                settingsIcon.setOnMouseClicked(event -> handleSettingsIconClick());

//                chefImage.getStyleClass().add("image-view");
//                titleText.getStyleClass().add("title-text");
//                subtitleText.getStyleClass().add("subtitle-text");
//                exploreButton.getStyleClass().add("button");
//
//                leftSpacer.prefWidthProperty().bind(newScene.widthProperty().multiply(0.25));
//                bottomSpacer.prefHeightProperty().bind(newScene.heightProperty().subtract(chefImage.getImage().getHeight()).subtract(titleText.getLayoutBounds().getHeight() + subtitleText.getLayoutBounds().getHeight() + exploreButton.getHeight()));
            }
        });
    }

    private void handleNotificationIconClick() {
        // Handle notification icon click
        System.out.println("Notification icon clicked!");
    }

    private void handleProfileIconClick() {
        // Handle profile icon click
        System.out.println("Profile icon clicked!");
    }

    private void handleSettingsIconClick() {
        // Handle settings icon click
        System.out.println("Settings icon clicked!");
    }
}