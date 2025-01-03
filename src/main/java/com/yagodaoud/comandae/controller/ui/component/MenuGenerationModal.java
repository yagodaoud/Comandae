package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MenuGenerationModal extends VBox {
    private final List<MenuItem> menuItems;
    private final Runnable onClose;
    private final Runnable onGenerate;
    private final Consumer<MenuItem> onFavorite;
    private final List<MenuItem> selectedItemsOrder = new ArrayList<>();
    private TextField searchField;

    public MenuGenerationModal(
            List<MenuItem> items,
            Runnable onCloseHandler,
            Runnable onGenerateHandler,
            Consumer<MenuItem> onFavoriteHandler
    ) {
        this.menuItems = items;
        this.onClose = onCloseHandler;
        this.onGenerate = onGenerateHandler;
        this.onFavorite = onFavoriteHandler;

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

        searchField = new TextField();
        searchField.setPromptText("Search items...");
        searchField.getStyleClass().add("search-field-menu");
        searchField.setMaxWidth(200);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterItems(newValue));

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

        populateCategories(sortedCategories, contentBox);

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

        getChildren().addAll(title, searchField, scrollPane, buttonBox);
    }

    public List<MenuItem> getSelectedItems() {
        return new ArrayList<>(selectedItemsOrder);
    }

    private void populateCategories(List<Map.Entry<MenuCategory, List<MenuItem>>> sortedCategories, VBox contentBox) {
        contentBox.getChildren().clear();

        for (Map.Entry<MenuCategory, List<MenuItem>> entry : sortedCategories) {
            MenuCategory category = entry.getKey();
            List<MenuItem> items = entry.getValue();

            Label categoryLabel = new Label(category.getName());
            categoryLabel.getStyleClass().add("category-header");

            VBox itemsBox = new VBox();
            itemsBox.setSpacing(8);
            items.stream()
                    .sorted(Comparator.comparing(
                            item -> item.getName() != null ? item.getName() : "",
                            String.CASE_INSENSITIVE_ORDER
                    ))
                    .forEach(item -> {
                        HBox itemLayout = new HBox();
                        itemLayout.setAlignment(Pos.CENTER_LEFT);
                        itemLayout.setSpacing(10);

                        String itemName = item.getName() != null ? item.getName() : "Unnamed Item";
                        CheckBox checkBox = new CheckBox(itemName);
                        checkBox.setUserData(item);
                        checkBox.getStyleClass().add("menu-item-checkbox");

                        checkBox.setSelected(selectedItemsOrder.contains(item) || item.isFavorite());

                        if (item.isFavorite()) {
                            selectedItemsOrder.add(item);
                        }

                        checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                            MenuItem menuItem = (MenuItem) checkBox.getUserData();
                            if (isSelected) {
                                if (!selectedItemsOrder.contains(menuItem)) {
                                    selectedItemsOrder.add(menuItem);
                                }
                            } else {
                                selectedItemsOrder.remove(menuItem);
                            }
                        });
                        Label favoriteLabel = new Label(
                                item.isFavorite() ? "\uE838" : "\uE83A"
                        );
                        favoriteLabel.getStyleClass().add("favorite-icon");

                        favoriteLabel.setOnMouseClicked(event -> {
                            boolean newFavoriteStatus = !item.isFavorite();
                            item.setFavorite(newFavoriteStatus);

                            favoriteLabel.setText(newFavoriteStatus ? "\uE838" : "\uE83A");

                            if (onFavorite != null) {
                                onFavorite.accept(item);
                            }
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        if (item.getPrice() != null && item.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                            Label priceLabel = new Label(String.format("R$ %.2f", item.getPrice()));
                            priceLabel.getStyleClass().add("price-label");

                            itemLayout.getChildren().addAll(
                                    checkBox,
                                    spacer,
                                    priceLabel,
                                    favoriteLabel
                            );
                        } else {
                            itemLayout.getChildren().addAll(
                                    checkBox,
                                    spacer,
                                    favoriteLabel
                            );
                        }

                        itemsBox.getChildren().add(itemLayout);
                    });

            contentBox.getChildren().addAll(categoryLabel, itemsBox);
        }
    }

    private void filterItems(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
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

            VBox contentBox = (VBox) ((ScrollPane) getChildren().get(2)).getContent();
            populateCategories(sortedCategories, contentBox);
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase().trim();

        Set<MenuItem> currentlySelectedItems = new HashSet<>(selectedItemsOrder);

        Map<MenuCategory, List<MenuItem>> filteredItemsByCategory = menuItems.stream()
                .filter(menuItem ->
                        menuItem.getName().toLowerCase().contains(lowerCaseSearchText) ||
                                (menuItem.getDescription() != null && menuItem.getDescription().toLowerCase().contains(lowerCaseSearchText)) ||
                                menuItem.getCategory().getName().toLowerCase().contains(lowerCaseSearchText)
                )
                .collect(Collectors.groupingBy(MenuItem::getCategory));

        List<Map.Entry<MenuCategory, List<MenuItem>>> sortedFilteredCategories = filteredItemsByCategory.entrySet()
                .stream()
                .sorted((a, b) -> {
                    Integer orderA = a.getKey().getDisplayOrder() != null ? a.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                    Integer orderB = b.getKey().getDisplayOrder() != null ? b.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                    return orderA.compareTo(orderB);
                })
                .collect(Collectors.toList());

        selectedItemsOrder.clear();
        selectedItemsOrder.addAll(currentlySelectedItems);

        VBox contentBox = (VBox) ((ScrollPane) getChildren().get(2)).getContent();
        populateCategories(sortedFilteredCategories, contentBox);
    }
}