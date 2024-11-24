package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuGenerationModal extends VBox {
    private final List<MenuItem> menuItems;
    private final Runnable onClose;
    private final Runnable onGenerate;
    private final List<MenuItem> selectedItemsOrder = new ArrayList<>();

    public MenuGenerationModal(
            List<MenuItem> items,
            Runnable onCloseHandler,
            Runnable onGenerateHandler
    ) {
        this.menuItems = items;
        this.onClose = onCloseHandler;
        this.onGenerate = onGenerateHandler;

        setupUI();
    }

    private void setupUI() {
        getStyleClass().add("modal-form");
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(500);
        setMaxHeight(600);

        Text title = new Text("Generate Menu");
        title.getStyleClass().add("modal-title");

        Map<MenuCategory, List<MenuItem>> itemsByCategory = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));

        List<Map.Entry<MenuCategory, List<MenuItem>>> sortedCategories = itemsByCategory.entrySet()
                .stream()
                .sorted((a, b) -> {
                    Integer orderA = a.getKey().getDisplayOrder() != null ? a.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                    Integer orderB = b.getKey().getDisplayOrder() != null ? b.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                    return orderA.compareTo(orderB);
                })
                .collect(Collectors.toList());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("menu-generation-scroll");

        VBox contentBox = new VBox();
        contentBox.setSpacing(15);
        contentBox.setPadding(new Insets(10));

        for (Map.Entry<MenuCategory, List<MenuItem>> entry : sortedCategories) {
            MenuCategory category = entry.getKey();
            List<MenuItem> items = entry.getValue();

            Label categoryLabel = new Label(category.getName());
            categoryLabel.getStyleClass().add("category-header");

            VBox itemsBox = new VBox();
            itemsBox.setSpacing(8);

            items.stream()
                    .sorted(Comparator.comparing(MenuItem::getName, String.CASE_INSENSITIVE_ORDER))
                    .forEach(item -> {
                        CheckBox checkBox = new CheckBox(item.getName());
                        checkBox.setUserData(item);
                        checkBox.getStyleClass().add("menu-item-checkbox");

                        checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                            MenuItem menuItem = (MenuItem) checkBox.getUserData();
                            if (isSelected) {
                                selectedItemsOrder.add(menuItem);
                            } else {
                                selectedItemsOrder.remove(menuItem);
                            }
                        });

                        if (item.getPrice() != null && item.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                            HBox itemBox = new HBox();
                            itemBox.setSpacing(10);
                            itemBox.setAlignment(Pos.CENTER_LEFT);

                            Label priceLabel = new Label(String.format("R$ %.2f", item.getPrice()));
                            priceLabel.getStyleClass().add("price-label");

                            itemBox.getChildren().addAll(checkBox, priceLabel);
                            itemsBox.getChildren().add(itemBox);
                        } else {
                            itemsBox.getChildren().add(checkBox);
                        }
                    });

            contentBox.getChildren().addAll(categoryLabel, itemsBox);
        }

        scrollPane.setContent(contentBox);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("secondary-button");
        cancelButton.setOnAction(e -> onClose.run());

        Button generateButton = new Button("Generate Menu");
        generateButton.getStyleClass().add("primary-button");
        generateButton.setOnAction(e -> onGenerate.run());

        buttonBox.getChildren().addAll(cancelButton, generateButton);

        getChildren().addAll(title, scrollPane, buttonBox);
    }

    public List<MenuItem> getSelectedItems() {
        return new ArrayList<>(selectedItemsOrder);
    }

    private List<CheckBox> getAllCheckBoxes() {
        VBox contentBox = (VBox) ((ScrollPane) getChildren().get(1)).getContent();
        return contentBox.getChildren().stream()
                .filter(node -> node instanceof VBox)
                .flatMap(node -> ((VBox) node).getChildren().stream())
                .flatMap(node -> {
                    if (node instanceof CheckBox) {
                        return Stream.of((CheckBox) node);
                    } else if (node instanceof HBox) {
                        return ((HBox) node).getChildren().stream()
                                .filter(child -> child instanceof CheckBox)
                                .map(child -> (CheckBox) child);
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toList());
    }
}