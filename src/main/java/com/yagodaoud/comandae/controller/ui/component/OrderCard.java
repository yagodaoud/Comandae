package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.Order;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class OrderCard extends VBox {
    @FXML
    private Label orderSlipLabel;
    @FXML private Label totalLabel;
    @FXML private Label timeLabel;
    @FXML private Label customerLabel;
    @FXML private Label paymentTypeLabel;
    @FXML private Button actionButton;
    @FXML private VBox productsContainer;

    public OrderCard(Order order) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/OrderCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            setupOrderData(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupOrderData(Order order) {
        orderSlipLabel.setText("Order #" + order.getOrderSlipId());
        totalLabel.setText("$" + order.getTotal());
        timeLabel.setText(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        if (order.getCustomer() != null) {
            customerLabel.setText(order.getCustomer().getName());
        }

        if (order.getPaymentType() == null) {
            paymentTypeLabel.setText("N/A");
        } else {
            paymentTypeLabel.setText(order.getPaymentType().toString());
        }

        if (order.getActive()) {
            actionButton.setText("\ue5ca"); // Material icon for done
            actionButton.setVisible(true);
        } else {
            actionButton.setVisible(false);
        }

        order.getOrderProducts().forEach(product -> {
            Label productLabel = new Label(product.getQuantity() + "x " + product.getProduct().getName());
            productLabel.getStyleClass().add("description");
            productsContainer.getChildren().add(productLabel);
        });
    }

    public void setOnActionButton(EventHandler<ActionEvent> handler) {
        actionButton.setOnAction(handler);
    }
}