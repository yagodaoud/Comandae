package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.BitcoinDTO;
import com.yagodaoud.comandae.model.Bitcoin;
import com.yagodaoud.comandae.model.BitcoinNetwork;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class BitcoinMethodModalForm extends VBox {
    private final TextField addressField;
    private final ComboBox<BitcoinNetwork> networkComboBox;
    private final Button saveButton;
    private final VBox walletRecordsBox;
    private final List<Bitcoin> existingWallets;
    private final Consumer<Bitcoin> onDeleteWallet;

    public BitcoinMethodModalForm(
            List<BitcoinNetwork> networks,
            List<Bitcoin> existingWallets,
            Runnable onCancel,
            java.util.function.Consumer<BitcoinDTO> onSave,
            Consumer<Bitcoin> onDeleteWallet
    ) {
        this.existingWallets = existingWallets;
        this.onDeleteWallet = onDeleteWallet;

        getStyleClass().add("modal-form");
        setSpacing(15);
        setPadding(new Insets(20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Bitcoin Wallet Registration");
        title.getStyleClass().add("modal-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label closeButton = new Label("\uE5CD");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnMouseClicked(e -> onCancel.run());

        headerBox.getChildren().addAll(title, spacer, closeButton);

        Label addressLabel = new Label("Address:");
        addressLabel.getStyleClass().add("select-label");
        addressField = new TextField();
        addressField.setPromptText("Bitcoin Wallet Address");

        Label networkLabel = new Label("Network:");
        networkLabel.getStyleClass().add("select-label");
        networkComboBox = new ComboBox<>();
        networkComboBox.getItems().addAll(networks);
        networkComboBox.setPromptText("Select Network");
        networkComboBox.getStyleClass().add("combo-box");

        saveButton = new Button("Save");
        saveButton.getStyleClass().addAll("button", "primary-button");
        saveButton.setOnAction(e -> {
            String address = addressField.getText().trim();
            BitcoinNetwork network = networkComboBox.getValue();

            if (!address.isEmpty() && network != null) {
                BitcoinDTO bitcoinDTO = new BitcoinDTO();
                bitcoinDTO.setAddress(address);
                bitcoinDTO.setNetwork(network);
                bitcoinDTO.setIsActive(true);
                bitcoinDTO.setCreatedAt(LocalDateTime.now());

                onSave.accept(bitcoinDTO);

                Bitcoin newWallet = new Bitcoin();
                newWallet.setAddress(address);
                newWallet.setNetwork(network);

                existingWallets.add(newWallet);

                populateWalletRecords(existingWallets);

            }
        });
        saveButton.setDisable(true);

        addressField.textProperty().addListener((obs, old, newValue) -> validateInput());
        networkComboBox.valueProperty().addListener((obs, old, newValue) -> validateInput());

        Text recordsTitle = new Text("Wallets");
        recordsTitle.getStyleClass().add("modal-subtitle");

        walletRecordsBox = new VBox(10);
        walletRecordsBox.getStyleClass().add("wallet-records");
        populateWalletRecords(existingWallets);

        getChildren().addAll(
                headerBox,
                addressLabel,
                addressField,
                networkLabel,
                networkComboBox,
                saveButton,
                recordsTitle,
                walletRecordsBox
        );
    }

    private void validateInput() {
        boolean isValid = !addressField.getText().trim().isEmpty()
                && networkComboBox.getValue() != null;
        saveButton.setDisable(!isValid);
    }

    private void populateWalletRecords(List<Bitcoin> wallets) {
        walletRecordsBox.getChildren().clear();

        for (Bitcoin wallet : wallets) {
            HBox walletRecord = new HBox(10);
            walletRecord.getStyleClass().add("wallet-record");
            walletRecord.setAlignment(Pos.CENTER_LEFT);

            Label networkLabel = new Label(wallet.getNetwork().toString());
            networkLabel.getStyleClass().add("wallet-record-label");

            Label addressLabel = new Label(formatBitcoinAddress(wallet.getAddress()));
            addressLabel.getStyleClass().add("wallet-record-address");
            HBox.setHgrow(addressLabel, Priority.ALWAYS);

            Label deleteLabel = new Label("\uE872");
            deleteLabel.getStyleClass().add("delete-button");
            deleteLabel.setOnMouseClicked(e -> {
                walletRecordsBox.getChildren().remove(walletRecord);

                existingWallets.remove(wallet);

                onDeleteWallet.accept(wallet);
            });


            walletRecord.getChildren().addAll(networkLabel, addressLabel, deleteLabel);
            walletRecordsBox.getChildren().add(walletRecord);
        }
    }

    private String formatBitcoinAddress(String bitcoinAddress) {

        if (bitcoinAddress.length() < 20) return bitcoinAddress;

        return bitcoinAddress.substring(0, 20) + "..." + bitcoinAddress.substring(bitcoinAddress.length() - 4);
    }
}