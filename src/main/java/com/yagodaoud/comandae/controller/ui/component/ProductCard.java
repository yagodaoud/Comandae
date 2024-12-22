package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.Product;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ProductCard extends HBox {

    private final Label productNameLabel;
    private final Label productPriceLabel;
    private final Button editButton;

    public ProductCard(Product product, Consumer<Product> onEdit) {
        super(10);
        getStyleClass().add("product-item");
        setAlignment(Pos.CENTER_LEFT);
        setFocusTraversable(false);

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

        getChildren().addAll(contentBox, spacer, editButton);
    }
}
