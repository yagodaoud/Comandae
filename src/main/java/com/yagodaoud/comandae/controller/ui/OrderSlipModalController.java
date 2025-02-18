package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.OrderService;
import com.yagodaoud.comandae.service.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OrderSlipModalController extends GridPane {

    @FXML private Text orderTitle;
    @FXML private Button closeButton;
    @FXML private Button saveButton;
    @FXML private Button checkoutButton;
    @FXML private VBox modalRoot;
    @FXML private StackPane modalOverlay;
    @FXML private FlowPane productsFlowPane;
    @FXML private VBox orderItemsContainer;
    @FXML private Text subtotalText;
    @FXML private Text totalText;

    @FXML private StackPane paymentModal;

    private int orderSlipId;
    private Order order;
    private List<Product> products;
    private final int GRID_COLUMNS = 5;
    private Runnable onClose;

    @Autowired private OrderService orderService;
    @Autowired private ProductService productService;

    @Autowired private PaymentModalController paymentModalController;

    @FXML
    private void initialize() {
        loadProducts();
        setupButtonCursors();
        paymentModal.setVisible(false);
    }

    private void setupButtonCursors() {
        closeButton.setCursor(javafx.scene.Cursor.HAND);
        saveButton.setCursor(javafx.scene.Cursor.HAND);
        checkoutButton.setCursor(javafx.scene.Cursor.HAND);
    }

    private void loadProducts() {
        products = productService.getAll(false);
        populateProductGrid();
    }

    private void populateProductGrid() {
        productsFlowPane.getChildren().clear();
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsFlowPane.getChildren().add(productCard);
        }
    }

    private VBox createProductCard(Product product) {
        VBox productCard = new VBox(10);
        productCard.getStyleClass().add("product-card");
        productCard.setPadding(new Insets(10));
        productCard.setAlignment(Pos.CENTER);

        ImageView productImage = createProductImage(product);
        Text nameText = new Text(product.getName());
        nameText.getStyleClass().add("product-name");

        Text priceText = new Text(formatPrice(product.getPrice()));
        priceText.getStyleClass().add("product-price");

        Button addButton = new Button("Add to Order");
        addButton.getStyleClass().add("add-button");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setCursor(javafx.scene.Cursor.HAND);

        if (!product.getHasInfiniteStock() && product.getStockQuantity() <= 0) {
            addButton.setDisable(true);
            addButton.setText("Out of Stock");
        }

        addButton.setOnAction(e -> handleAddToOrder(product));

        productCard.getChildren().addAll(productImage, nameText, priceText, addButton);
        addHoverEffect(productCard);

        return productCard;
    }

    private ImageView createProductImage(Product product) {
        ImageView productImage = new ImageView();
        productImage.setFitHeight(170);
        productImage.setFitWidth(170);
        productImage.setPreserveRatio(true);

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(product.getImage());
                Image image = new Image(new ByteArrayInputStream(imageData));
                productImage.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            productImage.setImage(new Image("/images/default-image.png"));
        }

        return productImage;
    }

    private void handleAddToOrder(Product product) {
        HBox orderItem = createOrderItemUI(product, 1);
        orderItemsContainer.getChildren().add(orderItem);
        updateOrderTotal();
    }

    private HBox createOrderItemUI(Product product, int quantity) {
        HBox orderItem = new HBox(10);
        orderItem.getStyleClass().add("order-item");
        orderItem.setAlignment(Pos.CENTER_LEFT);

        ImageView itemImage = createProductImage(product);

        VBox detailsBox = new VBox(5);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        Text itemName = new Text(product.getName());
        itemName.getStyleClass().add("item-name");

        HBox quantityAndPrice = new HBox(10);
        quantityAndPrice.setAlignment(Pos.CENTER_LEFT);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, quantity);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(80);

        if (!product.getHasInfiniteStock()) {
            quantitySpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.getStockQuantity(), quantity)
            );
        }

        Text itemPrice = new Text(formatPrice(product.getPrice()));
        itemPrice.getStyleClass().add("item-price");

        quantityAndPrice.getChildren().addAll(quantitySpinner, itemPrice);
        detailsBox.getChildren().addAll(itemName, quantityAndPrice);

        Button removeButton = new Button("×");
        removeButton.getStyleClass().add("remove-button");
        removeButton.setCursor(javafx.scene.Cursor.HAND);
        removeButton.setOnAction(e -> {
            orderItemsContainer.getChildren().remove(orderItem);
            updateOrderTotal();
        });

        orderItem.getChildren().addAll(itemImage, detailsBox, removeButton);

        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateOrderTotal());

        return orderItem;
    }

    private void updateOrderTotal() {
        BigDecimal subtotal = calculateSubtotal();
        subtotalText.setText(formatPrice(subtotal));
        totalText.setText(formatPrice(subtotal));
    }

    private BigDecimal calculateSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Node node : orderItemsContainer.getChildren()) {
            if (node instanceof HBox orderItem) {
                VBox detailsBox = (VBox) orderItem.getChildren().get(1);
                HBox quantityAndPrice = (HBox) detailsBox.getChildren().get(1);
                Spinner<Integer> spinner = (Spinner<Integer>) quantityAndPrice.getChildren().get(0);
                Text priceText = (Text) quantityAndPrice.getChildren().get(1);

                BigDecimal price = new BigDecimal(priceText.getText().replace("$", "").replace(",", "."));
                subtotal = subtotal.add(price.multiply(new BigDecimal(spinner.getValue())));
            }
        }

        return subtotal;
    }

    private void addHoverEffect(VBox productCard) {
        productCard.setOnMouseEntered(e -> {
            productCard.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 0);");
        });

        productCard.setOnMouseExited(e -> {
            productCard.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 0);");
        });
    }

    private String formatPrice(BigDecimal price) {
        return String.format("$%.2f", price);
    }

    @FXML
    private void handleSaveOrder() {
        saveOrder();
        showAlert("Success", "Order saved successfully!", Alert.AlertType.INFORMATION);
        closeModal();
    }

    @FXML
    private void handleCheckout() {
        paymentModalController.setOrder(order);
        paymentModalController.setOrderSlipModalController(this);
        paymentModalController.showModal();
        saveOrder();
    }

    private void saveOrder() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : orderItemsContainer.getChildren()) {
            if (node instanceof HBox orderItem) {
                VBox detailsBox = (VBox) orderItem.getChildren().get(1);
                Text itemName = (Text) detailsBox.getChildren().get(0);
                HBox quantityAndPrice = (HBox) detailsBox.getChildren().get(1);
                Spinner<Integer> spinner = (Spinner<Integer>) quantityAndPrice.getChildren().get(0);
                Text priceText = (Text) quantityAndPrice.getChildren().get(1);

                Product product = products.stream()
                        .filter(p -> p.getName().equals(itemName.getText()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemName.getText()));

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(spinner.getValue());
                orderProduct.setOrder(order);

                BigDecimal price = new BigDecimal(priceText.getText().replace("$", "").replace(",", "."));
                total = total.add(price.multiply(new BigDecimal(spinner.getValue())));

                orderProducts.add(orderProduct);
            }
        }

        order.setOrderProducts(orderProducts);
        order.setTotal(total);
        order.setActive(true);  // Order is active until checkout

        orderService.save(order);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setOrderSlipId(int orderSlipId) {
        this.orderSlipId = orderSlipId;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public void showModal() {
        products = productService.getAll(false);
        orderItemsContainer.getChildren().clear();

        orderTitle.setText("New order #" + orderSlipId);

        try {
            order = orderService.findByOrderSlipId(orderSlipId);

            if (order != null && order.getOrderProducts() != null) {

                orderTitle.setText("Order Slip #" + orderSlipId);

                for (OrderProduct orderProduct : order.getOrderProducts()) {
                    Product product = orderProduct.getProduct();

                    if (product != null) {
                        HBox orderItemUI = createOrderItemUI(product, orderProduct.getQuantity());
                        orderItemsContainer.getChildren().add(orderItemUI);
                    }
                }
                updateOrderTotal();
            }
        } catch (Exception ex) {
            order = new Order();
            order.setOrderSlipId(orderSlipId);
        }

        paymentModalController.setOrder(order);

        modalOverlay.toFront();
        modalOverlay.setVisible(true);
    }

    public void closeModal() {
        modalOverlay.setVisible(false);

        orderItemsContainer.getChildren().clear();
        order = new Order();
        order.setOrderSlipId(orderSlipId);

        subtotalText.setText("$0.00");
        totalText.setText("$0.00");

        updateOrderTotal();

    }
}