package com.yagodaoud.comandae.controller.ui;


import com.yagodaoud.comandae.controller.ui.component.GlobalBarcodeScanner;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;

public abstract class BarcodeAwareController {
    protected GlobalBarcodeScanner barcodeScanner = GlobalBarcodeScanner.getInstance();

    @FXML
    public void initialize() {
        Region root = getRoot();
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                barcodeScanner.setupBarcodeReader(newScene);
                if (this instanceof GlobalBarcodeScanner.BarcodeEventListener) {
                    barcodeScanner.addListener((GlobalBarcodeScanner.BarcodeEventListener) this);
                }
            }
        });
    }

    protected abstract Region getRoot();

    public void cleanup() {
        if (this instanceof GlobalBarcodeScanner.BarcodeEventListener) {
            barcodeScanner.removeListener((GlobalBarcodeScanner.BarcodeEventListener) this);
        }
    }
}