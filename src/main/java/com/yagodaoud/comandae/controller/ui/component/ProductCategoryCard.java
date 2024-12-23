package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.Category;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ProductCategoryCard extends HBox {

    private final Label nameLabel;
    private final Label itemCountLabel;
    private final Button editButton;

    public ProductCategoryCard(Category category, Consumer<Category> onEdit) {
        super(10);
        getStyleClass().add("category-item");
        setAlignment(Pos.CENTER_LEFT);
        setFocusTraversable(false);

        VBox contentBox = new VBox(5);
        nameLabel = new Label(category.getName());
        itemCountLabel = new Label((category.getProductCount() == null ? 0 : category.getProductCount()) + " items");
        itemCountLabel.getStyleClass().add("item-count");
        contentBox.getChildren().addAll(nameLabel, itemCountLabel);

        editButton = new Button("\uF88D");
        editButton.getStyleClass().add("edit-button");
        editButton.setCursor(Cursor.HAND);
        editButton.setOnAction(e -> onEdit.accept(category));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(contentBox, spacer, editButton);
    }
}
