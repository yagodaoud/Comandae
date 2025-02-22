package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.controller.ui.component.OrderCard;
import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.service.OrderService;
import com.yagodaoud.comandae.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderManagementController {

    @FXML
    private VBox activeOrdersContainer;
    @FXML
    private VBox historyOrdersContainer;

    @Autowired
    private OrderService orderService;

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @FXML
    private void initialize() {
        sidebarController.setSelectedScreen(NavigationScreen.ORDER_MANAGEMENT);
        loadOrders();
    }

    private void loadOrders() {
        List<Order> activeOrders = orderService.findByActiveTrue();
        List<Order> historicalOrders = orderService.findByActiveFalse();

        activeOrdersContainer.getChildren().clear();
        historyOrdersContainer.getChildren().clear();

        activeOrders.forEach(order -> {
            OrderCard orderCard = new OrderCard(order);
            orderCard.setOnActionButton(e -> handleOrderAction(order));
            activeOrdersContainer.getChildren().add(orderCard);
        });

        historicalOrders.forEach(order -> {
            OrderCard orderCard = new OrderCard(order);
            historyOrdersContainer.getChildren().add(orderCard);
        });
    }

    private void handleOrderAction(Order order) {
        order.setActive(false);
        order.setDeletedAt(LocalDateTime.now());
        orderService.save(order);
        loadOrders();
    }

}