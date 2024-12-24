package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.OrderService;
import com.yagodaoud.comandae.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderSlipModalController extends GridPane {

    private int orderSlipId;

    private Order order;

    @FXML
    public Button closeButton;

    @FXML
    private VBox modalRoot;

    @FXML
    private StackPane modalOverlay;

    @FXML
    private VBox mainContainer;

    @FXML
    private Text barcodeText;

    @FXML
    private TextField quantityField;

    @FXML
    private TextArea notesField;

    private Runnable onClose;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @FXML
    private void initialize() {
        setHgap(10);
        setVgap(15);
    }


    public void setOrderSlipId(Integer orderSlipId) {
        this.orderSlipId = orderSlipId;

//        barcodeText.setText("Barcode: " + orderSlipId);
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public void showModal() {

        List<Product> productList = productService.getAll(false);

        try {
            order = orderService.findByOrderSlipId(orderSlipId);

            //populate with order
        } catch (EntityNotFoundException ex) {


        }



        modalOverlay.toFront();

        modalOverlay.setVisible(true);
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
        String quantity = quantityField.getText();
        String notes = notesField.getText();

        closeModal();
    }

    public void clearFields() {
        quantityField.clear();
        notesField.clear();
        barcodeText.setText("");
    }

    public void handleCheckout() {

    }
}