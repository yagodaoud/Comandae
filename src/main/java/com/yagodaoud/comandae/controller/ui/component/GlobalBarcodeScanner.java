package com.yagodaoud.comandae.controller.ui.component;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class GlobalBarcodeScanner {
    private StringBuilder barcodeBuilder = new StringBuilder();
    private Instant lastKeyPress;
    private static final long BARCODE_TIMEOUT_MS = 100;
    private List<BarcodeEventListener> listeners = new ArrayList<>();
    private static GlobalBarcodeScanner instance;

    public static GlobalBarcodeScanner getInstance() {
        if (instance == null) {
            instance = new GlobalBarcodeScanner();
        }
        return instance;
    }

    public interface BarcodeEventListener {
        void onBarcodeScanned(int barcode);
    }

    public void addListener(BarcodeEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(BarcodeEventListener listener) {
        listeners.remove(listener);
    }

    public void setupBarcodeReader(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            Instant now = Instant.now();

            if (lastKeyPress != null &&
                    Duration.between(lastKeyPress, now).toMillis() > BARCODE_TIMEOUT_MS) {
                barcodeBuilder.setLength(0);
            }

            lastKeyPress = now;

            if (event.getCode().toString().equals("ENTER")) {
                if (barcodeBuilder.length() > 0) {
                    int barcode = Integer.parseInt(barcodeBuilder.toString());
                    notifyListeners(barcode);
                    barcodeBuilder.setLength(0);
                    event.consume();
                }
            } else {
                barcodeBuilder.append(event.getText());
            }
        });
    }

    private void notifyListeners(int barcode) {
        Platform.runLater(() -> {
            for (BarcodeEventListener listener : listeners) {
                listener.onBarcodeScanned(barcode);
            }
        });
    }
}