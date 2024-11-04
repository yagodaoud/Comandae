package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class EntryScreenController {

    private final StageManager stageManager;

    @Autowired
    public EntryScreenController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private ImageView chefImage;

    @FXML
    private Text titleText;

    @FXML
    private Text subtitleText;

    @FXML
    private Button exploreButton;

    @FXML
    private Region leftSpacer;

    @FXML
    private Region bottomSpacer;

    @FXML
    public void initialize() {

        chefImage.setImage(new Image("https://i.pinimg.com/originals/0d/bb/dd/0dbbdd2944e06b540a3341c8d6a801e4.jpg"));

        chefImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                String cssPath = getClass().getResource("/css/entryScreen.css").toExternalForm();
                newScene.getStylesheets().add(cssPath);

                chefImage.getStyleClass().add("image-view");
                titleText.getStyleClass().add("title-text");
                subtitleText.getStyleClass().add("subtitle-text");
                exploreButton.getStyleClass().add("button");

                leftSpacer.prefWidthProperty().bind(newScene.widthProperty().multiply(0.25));
                bottomSpacer.prefHeightProperty().bind(newScene.heightProperty().subtract(chefImage.getImage().getHeight()).subtract(titleText.getLayoutBounds().getHeight() + subtitleText.getLayoutBounds().getHeight() + exploreButton.getHeight()));
            }
        });
    }

    @FXML
    public void handleJoinButton() {
        stageManager.switchScene("view/MainScreen.fxml", "Main Screen");
    }
}
