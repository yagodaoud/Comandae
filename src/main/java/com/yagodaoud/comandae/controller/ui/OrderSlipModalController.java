package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.OrderService;
import com.yagodaoud.comandae.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OrderSlipModalController extends GridPane {

    @FXML
    public Text orderTitle;

    private int orderSlipId;

    private Order order;

    @FXML
    public Button closeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button checkoutButton;

    @FXML
    private VBox modalRoot;

    @FXML
    private StackPane modalOverlay;

    @FXML
    private GridPane productsGrid;

    @FXML
    private VBox orderItemsContainer;

    @FXML
    private Text subtotalText;

    @FXML
    private Text totalText;

    private List<Product> products;
    private final int GRID_COLUMNS = 5;

    private Runnable onClose;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @FXML
    private void initialize() {
        loadProducts();
    }

    @FXML
    private void handleSaveOrder() {
        saveOrder(null);
        showAlert("Success", "Order saved successfully!", Alert.AlertType.INFORMATION);
    }

    private void loadProducts() {
        products = productService.getAll(false);
        populateProductGrid();
    }

    private void populateProductGrid() {
        productsGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Product product : products) {
            VBox productCard = createProductCard(product);

            productsGrid.add(productCard, col, row);

            col++;
            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox productCard = new VBox(10);
        productCard.getStyleClass().add("product-card");
        productCard.setPadding(new Insets(10));
        productCard.setAlignment(Pos.CENTER);

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(177, 177);
        imageContainer.setMinSize(177, 177);
        imageContainer.setMaxSize(177, 177);

        ImageView productImage = new ImageView();
        productImage.setFitHeight(170);
        productImage.setFitWidth(170    );
        productImage.setPreserveRatio(true);

        productImage.setImage(new Image("/images/default-image.png"));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(product.getImage());
                Image image = new Image(new ByteArrayInputStream(imageData));
                productImage.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        imageContainer.getChildren().add(productImage);
        StackPane.setAlignment(productImage, Pos.CENTER);

        Text nameText = new Text(product.getName());
        nameText.getStyleClass().add("product-name");

        Text priceText = new Text(formatPrice(product.getPrice()));
        priceText.getStyleClass().add("product-price");

        Button addButton = new Button("Add to Order");
        addButton.getStyleClass().add("add-button");
        addButton.setMaxWidth(Double.MAX_VALUE);

        if (!product.getHasInfiniteStock() && product.getStockQuantity() <= 0) {
            addButton.setDisable(true);
            addButton.setText("Out of Stock");
        }

        addButton.setOnAction(e -> handleAddToOrder(product));

        productCard.getChildren().addAll(imageContainer, nameText, priceText, addButton);

        addHoverEffect(productCard);

        return productCard;
    }

    private void handleAddToOrder(Product product) {
        HBox orderItem = new HBox(10);
        orderItem.getStyleClass().add("order-item");
        orderItem.setAlignment(Pos.CENTER_LEFT);

        ImageView itemImage = new ImageView();
        itemImage.setFitHeight(50);
        itemImage.setFitWidth(50);
        itemImage.setPreserveRatio(true);

        itemImage.setImage(new Image("/images/default-image.png"));
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(product.getImage());
                Image image = new Image(new ByteArrayInputStream(imageData));
                itemImage.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        VBox detailsBox = new VBox(5);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        Text itemName = new Text(product.getName());
        itemName.getStyleClass().add("item-name");

        HBox quantityAndPrice = new HBox(10);
        quantityAndPrice.setAlignment(Pos.CENTER_LEFT);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(80);

        if (!product.getHasInfiniteStock()) {
            quantitySpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.getStockQuantity(), 1)
            );
        }

        Text itemPrice = new Text(formatPrice(product.getPrice()));
        itemPrice.getStyleClass().add("item-price");

        quantityAndPrice.getChildren().addAll(quantitySpinner, itemPrice);
        detailsBox.getChildren().addAll(itemName, quantityAndPrice);

        Button removeButton = new Button("×");
        removeButton.getStyleClass().add("remove-button");
        removeButton.setOnAction(e -> {
            orderItemsContainer.getChildren().remove(orderItem);
            updateOrderTotal();
        });

        orderItem.getChildren().addAll(itemImage, detailsBox, removeButton);

        orderItemsContainer.getChildren().add(orderItem);

        updateOrderTotal();

        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOrderTotal();
        });
    }

    private void updateOrderTotal() {
        BigDecimal subtotal = calculateSubtotal();

        subtotalText.setText(formatPrice(subtotal));
        totalText.setText(formatPrice(subtotal));
    }

    private BigDecimal calculateSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Node node : orderItemsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox orderItem = (HBox) node;
                VBox detailsBox = (VBox) orderItem.getChildren().get(1);
                HBox quantityAndPrice = (HBox) detailsBox.getChildren().get(1);

                Spinner<Integer> spinner = (Spinner<Integer>) quantityAndPrice.getChildren().get(0);
                Text priceText = (Text) quantityAndPrice.getChildren().get(1);

                BigDecimal price = new BigDecimal(priceText.getText().replace("$", "").replace(",", "."));
                Integer quantity = spinner.getValue();
                if (quantity != null) {
                    subtotal = subtotal.add(price.multiply(new BigDecimal(quantity)));
                }
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


    public void setOrderSlipId(Integer orderSlipId) {
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
        } catch (EntityNotFoundException ex) {
            order = new Order();
            order.setOrderSlipId(orderSlipId);
        }

        modalOverlay.toFront();
        modalOverlay.setVisible(true);
    }

    private HBox createOrderItemUI(Product product, int quantity) {
        HBox orderItem = new HBox(10);
        orderItem.getStyleClass().add("order-item");
        orderItem.setAlignment(Pos.CENTER_LEFT);

        // Product image
        ImageView itemImage = new ImageView();
        itemImage.setFitHeight(50);
        itemImage.setFitWidth(50);
        itemImage.setPreserveRatio(true);

        // Set image
        itemImage.setImage(new Image("/images/default-image.png"));
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(product.getImage());
                Image image = new Image(new ByteArrayInputStream(imageData));
                itemImage.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Product details container
        VBox detailsBox = new VBox(5);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        Text itemName = new Text(product.getName());
        itemName.getStyleClass().add("item-name");

        // Quantity spinner and price container
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
        removeButton.setOnAction(e -> {
            orderItemsContainer.getChildren().remove(orderItem);
            updateOrderTotal();
        });

        orderItem.getChildren().addAll(itemImage, detailsBox, removeButton);

        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOrderTotal();
        });

        return orderItem;
    }

    public void hideModal() {
        modalOverlay.setVisible(false);

        clearFields();
    }

    @FXML
    private void closeModal() {
        if (onClose != null) {
            onClose.run();
        }
        hideModal();
    }

    @FXML
    private void addToOrder() {

        closeModal();
    }

    public void clearFields() {
        orderItemsContainer.getChildren().clear();

        order = new Order();
        order.setOrderSlipId(orderSlipId);

        subtotalText.setText("$0.00");
        totalText.setText("$0.00");

//        saveButton.setDisable(false);
//        checkoutButton.setDisable(true);

        updateOrderTotal();
    }

    @FXML
    private void handleCheckout() {
        // Create checkout confirmation dialog
        Dialog<ButtonType> checkoutDialog = new Dialog<>();
        checkoutDialog.initStyle(StageStyle.UNDECORATED);
        checkoutDialog.setTitle("Checkout");

        // Create content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("checkout-modal");

        // Add order details
        Text itemsHeader = new Text("Order Summary");
        itemsHeader.getStyleClass().add("checkout-header");
        content.getChildren().add(itemsHeader);

        // List all items
        VBox itemsList = new VBox(5);
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : orderItemsContainer.getChildren()) {
            if (node instanceof HBox orderItem) {
                VBox detailsBox = (VBox) orderItem.getChildren().get(1);
                Text itemName = (Text) detailsBox.getChildren().get(0);
                HBox quantityAndPrice = (HBox) detailsBox.getChildren().get(1);
                Spinner<Integer> spinner = (Spinner<Integer>) quantityAndPrice.getChildren().get(0);
                Text priceText = (Text) quantityAndPrice.getChildren().get(1);

                BigDecimal price = new BigDecimal(priceText.getText().replace("$", "").replace(",", "."));
                BigDecimal itemTotal = price.multiply(new BigDecimal(spinner.getValue()));
                total = total.add(itemTotal);

                Text itemDetail = new Text(String.format("%s x%d - %s",
                        itemName.getText(),
                        spinner.getValue(),
                        formatPrice(itemTotal)));
                itemsList.getChildren().add(itemDetail);
            }
        }

        content.getChildren().add(itemsList);

        // Add total
        Text totalText = new Text(String.format("Total: %s", formatPrice(total)));
        totalText.getStyleClass().add("checkout-total");
        content.getChildren().add(totalText);

        // Add customer note field
        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Add note for the kitchen (optional)");
        noteArea.setPrefRowCount(3);
        content.getChildren().add(noteArea);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        checkoutDialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        // Style the dialog
        checkoutDialog.getDialogPane().setContent(content);
        checkoutDialog.getDialogPane().getStyleClass().add("checkout-dialog");

        // Handle result
        BigDecimal finalTotal = total;
        checkoutDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == confirmButton) {
                completeCheckout(noteArea.getText(), finalTotal);
            }
        });
    }

    private void completeCheckout(String note, BigDecimal total) {
        try {
            saveOrder(note);

            order.setActive(false);
            order.setTotal(total);
//            order.setCompletedAt(LocalDateTime.now());
            orderService.save(order);

            showAlert("Success", "Order has been checked out successfully!", Alert.AlertType.INFORMATION);
            closeModal();
        } catch (Exception e) {
            showAlert("Error", "Failed to complete checkout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void saveOrder(String note) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Node node : orderItemsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox orderItem = (HBox) node;
                VBox detailsBox = (VBox) orderItem.getChildren().get(1);
                Text itemName = (Text) detailsBox.getChildren().get(0);
                HBox quantityAndPrice = (HBox) detailsBox.getChildren().get(1);
                Spinner<Integer> spinner = (Spinner<Integer>) quantityAndPrice.getChildren().get(0);
                Text priceText = (Text) quantityAndPrice.getChildren().get(1);

                Product product = products.stream()
                        .filter(p -> p.getName().equals(itemName.getText()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemName.getText()));

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(spinner.getValue());
                orderProduct.setOrder(order);

                // Calculate item total
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

}