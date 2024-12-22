package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.Product;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.function.Consumer;

public class ProductCard extends HBox {

    private final Label productNameLabel;
    private final Label productPriceLabel;
    private final Button editButton;
    private final Product productEntity;
    private final ImageView productImage;

    public ProductCard(Product product, Consumer<Product> onEdit) {
        super(10);
        this.productEntity = product;
        getStyleClass().add("product-item");
        setAlignment(Pos.CENTER_LEFT);
        setFocusTraversable(false);

        productImage = new ImageView();
        productImage.setFitHeight(60);
        productImage.setFitWidth(60);
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

        VBox contentBox = new VBox(5);
        productNameLabel = new Label(product.getName());
        productPriceLabel = new Label(String.format("R$%.2f", product.getPrice()));
        productPriceLabel.getStyleClass().add("price-text");
        contentBox.getChildren().addAll(productNameLabel, productPriceLabel);

        editButton = new Button("\uF88D");
        editButton.getStyleClass().add("edit-button");
        editButton.setCursor(Cursor.HAND);
        editButton.setOnAction(e -> onEdit.accept(product));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(productImage, contentBox, spacer, editButton);

        productImage.getStyleClass().add("product-image");
    }

    public Product getProduct() {
        return this.productEntity;
    }

    public void updateProduct(Product updatedProduct) {
        this.productEntity.setName(updatedProduct.getName());
        this.productEntity.setPrice(updatedProduct.getPrice());

        if (updatedProduct.getCategory() != null) {
            this.productEntity.setCategory(updatedProduct.getCategory());
        }

        // Update image if changed
        if (updatedProduct.getImage() != null && !updatedProduct.getImage().isEmpty()) {
            try {
                byte[] imageData = Base64.getDecoder().decode(updatedProduct.getImage());
                Image image = new Image(new ByteArrayInputStream(imageData));
                productImage.setImage(image);
                this.productEntity.setImage(updatedProduct.getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        productNameLabel.setText(updatedProduct.getName());
        productPriceLabel.setText(String.format("R$%.2f", updatedProduct.getPrice()));
    }
}