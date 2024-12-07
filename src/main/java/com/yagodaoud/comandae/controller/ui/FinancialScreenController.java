package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.controller.ui.component.BitcoinMethodModalForm;
import com.yagodaoud.comandae.controller.ui.component.ModalContainer;
import com.yagodaoud.comandae.controller.ui.component.PixMethodModalForm;
import com.yagodaoud.comandae.model.*;
import com.yagodaoud.comandae.service.BitcoinService;
import com.yagodaoud.comandae.service.PixService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class FinancialScreenController {

    @FXML
    private StackPane modalContainer;

    private ModalContainer modal;

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @Autowired
    private PixService pixService;

    @Autowired
    private BitcoinService bitcoinService;

    List<BitcoinNetwork> networks;
    List<Bitcoin> existingWallets;

    List<PixType> pixTypes;
    List<Pix> existingPixKeys;

    @FXML
    public void initialize() {
        networks = bitcoinService.getAvailableNetworks();
        existingWallets = bitcoinService.getAll(false);

        pixTypes = pixService.getAvailableTypes();
        existingPixKeys = pixService.getAll(false);

        sidebarController.setSelectedScreen(NavigationScreen.FINANCIAL);

        modal = new ModalContainer();
        modalContainer.getChildren().add(modal);

        modalContainer.setPickOnBounds(false);
        modalContainer.toFront();
    }

    @FXML
    private void showBitcoinRegistrationModal(ActionEvent event) {
        BitcoinMethodModalForm form = new BitcoinMethodModalForm    (
                networks,
                existingWallets,
                () -> modal.hide(),
                bitcoinDTO -> {
                    Bitcoin savedBitcoin = bitcoinService.create(bitcoinDTO);
                    Platform.runLater(() -> {
                        existingWallets.add(savedBitcoin);
                        refreshBitcoinWalletsList();
                    });
                },
                bitcoinToDelete -> {
                    bitcoinService.delete(bitcoinToDelete.getId());
                    Platform.runLater(this::refreshBitcoinWalletsList);
                }
        );

        modal.show(form);
    }

    @FXML
    private void showPixRegistrationModal(ActionEvent event) {
        PixMethodModalForm form = new PixMethodModalForm(
                pixTypes,
                existingPixKeys,
                () -> modal.hide(),
                pixDTO -> {
                    Pix savedPix = pixService.create(pixDTO);
                    Platform.runLater(() -> {
                        existingPixKeys.add(savedPix);
                        refreshPixWalletsList();
                    });
                },
                pixToDelete -> {
                    pixService.delete(pixToDelete.getId());
                    Platform.runLater(this::refreshPixWalletsList);
                }
        );

        modal.show(form);
    }

    private void refreshBitcoinWalletsList() {
        existingWallets.clear();
        existingWallets.addAll(bitcoinService.getAll(false));
    }

    private void refreshPixWalletsList() {
        existingPixKeys.clear();
        existingPixKeys.addAll(pixService.getAll(false));
    }
}
