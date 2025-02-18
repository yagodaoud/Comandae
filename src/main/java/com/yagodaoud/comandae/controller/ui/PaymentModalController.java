package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.model.PaymentType;
import com.yagodaoud.comandae.model.Pix;
import com.yagodaoud.comandae.service.OrderService;
import com.yagodaoud.comandae.service.PixService;
import com.yagodaoud.comandae.utils.PixGenerator;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentModalController {

    @FXML private StackPane paymentModalContainer;
    @FXML private ComboBox<PaymentType> paymentTypeComboBox;
    @FXML private StackPane qrCodeContainer;
    @FXML private ImageView qrCodeImage;
    @FXML private Button markAsPaidButton;
    @FXML private Button closeButton;
    @FXML private VBox orderSummaryItems;
    @FXML private Text orderTotalText;

    private Order order;
    private OrderSlipModalController orderSlipModalController;

    private Pix pix;

    @Autowired private OrderService orderService;
    @Autowired private PixService pixService;

    @FXML
    private void initialize() {
        setupPaymentTypeComboBox();
        setupButtonActions();

        pix = pixService.getActivePix();
    }

    private void setupPaymentTypeComboBox() {
        paymentTypeComboBox.getItems().addAll(PaymentType.values());
        paymentTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handlePaymentTypeSelection(newVal);
            }
        });
    }

    private void handlePaymentTypeSelection(PaymentType paymentType) {
        qrCodeContainer.setVisible(false);

        switch (paymentType) {
            case PIX:
                try {
                    String pixCode = PixGenerator.generatePixCode(
                            pix.getKey(),
                            order.getTotal()
                    );
                    System.out.println(pix.toString());
                    System.out.println(pixCode);

                    Image qrImage = PixGenerator.generateQRCodeImage(pixCode, 250, 250);
                    qrCodeImage.setImage(qrImage);
                    qrCodeContainer.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BITCOIN:
                // Load Bitcoin QR code
                qrCodeImage.setImage(new Image("/images/bitcoin-qr.png"));
                qrCodeContainer.setVisible(true);
                break;
            case CASH:
                // No QR code for cash
                break;
        }
    }

    private void setupButtonActions() {
        markAsPaidButton.setOnAction(e -> handleMarkAsPaid());
        closeButton.setOnAction(e -> closeModal());
    }

    @FXML
    private void handleMarkAsPaid() {
        order.setActive(false);
        orderService.save(order);

        // Close both modals
        closeModal();
        if (orderSlipModalController != null) {
            orderSlipModalController.closeModal();
        }
    }

    @FXML
    private void closeModal() {
        paymentModalContainer.setVisible(false);
    }

    public void setOrder(Order order) {
        this.order = order;
        updateOrderSummary();
    }

    public void setOrderSlipModalController(OrderSlipModalController orderSlipModalController) {
        this.orderSlipModalController = orderSlipModalController;
    }

    public void showModal() {
        paymentModalContainer.setVisible(true);
        paymentModalContainer.toFront();
    }

    private void updateOrderSummary() {
        orderSummaryItems.getChildren().clear();

        if (order != null && order.getOrderProducts() != null) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                HBox itemRow = new HBox(10);
                itemRow.setAlignment(Pos.CENTER_LEFT);

                Text itemName = new Text(orderProduct.getProduct().getName());
                itemName.getStyleClass().add("summary-item-name");

                Text itemQuantity = new Text("x" + orderProduct.getQuantity());
                itemQuantity.getStyleClass().add("summary-item-quantity");

                Text itemPrice = new Text(formatPrice(orderProduct.getProduct().getPrice().multiply(new BigDecimal(orderProduct.getQuantity()))));
                itemPrice.getStyleClass().add("summary-item-price");

                itemRow.getChildren().addAll(itemName, itemQuantity, itemPrice);
                orderSummaryItems.getChildren().add(itemRow);
            }

            orderTotalText.setText(formatPrice(order.getTotal()));
        }
    }

    private String formatPrice(BigDecimal price) {
        return String.format("$%.2f", price);
    }
}