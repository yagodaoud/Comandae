    package com.yagodaoud.comandae.controller.ui;

    import com.yagodaoud.comandae.controller.ui.component.*;
    import com.yagodaoud.comandae.dto.menu.MenuCategoryDTO;
    import com.yagodaoud.comandae.dto.menu.MenuFooterDTO;
    import com.yagodaoud.comandae.dto.menu.MenuHeaderDTO;
    import com.yagodaoud.comandae.model.NavigationScreen;
    import com.yagodaoud.comandae.model.menu.MenuCategory;
    import com.yagodaoud.comandae.model.menu.MenuFooter;
    import com.yagodaoud.comandae.model.menu.MenuHeader;
    import com.yagodaoud.comandae.model.menu.MenuItem;
    import com.yagodaoud.comandae.service.menu.MenuCategoryService;
    import com.yagodaoud.comandae.service.menu.MenuFooterService;
    import com.yagodaoud.comandae.service.menu.MenuHeaderService;
    import com.yagodaoud.comandae.service.menu.MenuItemService;
    import javafx.animation.KeyFrame;
    import javafx.animation.KeyValue;
    import javafx.animation.Timeline;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.Node;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.ScrollPane;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.*;
    import javafx.util.Duration;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Lazy;
    import org.springframework.stereotype.Component;

    import java.math.BigDecimal;
    import java.util.*;
    import java.util.stream.Collectors;

    @Component
    @Lazy
    public class MenuScreenController {

        @FXML
        @Autowired
        private SidebarController sidebarController;

        @FXML
        private FlowPane menuItemFlowPane;

        @FXML
        private VBox sidebar;

        @FXML
        private VBox translateVbox;
        @FXML
        private StackPane sidebarHeader;
        @FXML
        private ScrollPane categoryScroll;

        @FXML
        private Button toggleButton;

        private boolean isSidebarExpanded = true;

        private static final double EXPANDED_WIDTH = 240;
        private static final double COLLAPSED_WIDTH = 0;
        private static final double ANIMATION_DURATION = 170;
        private static final double SIDEBAR_LEFT_ANCHOR = 205;

        @FXML
        private VBox categoriesList;

        @FXML
        private TextField itemSearchField;

        @Autowired
        private MenuHeaderService menuHeaderService;

        @Autowired
        private MenuFooterService menuFooterService;

        private String menuHeader = "";
        private String menuFooter = "";

        private MenuHeader currentMenuHeader;
        private MenuFooter currentMenuFooter;

        private ObservableList<MenuCategory> categories;
        private ObservableList<MenuItem> menuItems;
        private MenuCategory selectedCategory = null;

        @Autowired
        private MenuCategoryService menuCategoryService;

        @Autowired
        private MenuItemService menuItemService;

        @FXML
        private StackPane modalContainer;
        private ModalContainer modal;

        @FXML
        private void initialize() {
            sidebar.setPrefWidth(EXPANDED_WIDTH);
            sidebar.setMinWidth(0);
            menuItemFlowPane.setLayoutX(SIDEBAR_LEFT_ANCHOR + EXPANDED_WIDTH + 15);

            sidebar.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                        double screenHeight = newHeight.doubleValue();
                        double buttonHeight = toggleButton.getPrefHeight();

                        if (buttonHeight == 0) buttonHeight = 60;

                        toggleButton.setText("\uE5CB");
                        toggleButton.setLayoutY(((screenHeight - buttonHeight) / 2) - 110);
                    });
                }
            });

            itemSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterMenuItems(newValue);
            });

            sidebarHeader.maxWidthProperty().bind(sidebar.prefWidthProperty());

            sidebarController.setSelectedScreen(NavigationScreen.MENU);
            loadCategories();
            loadMenuItems();

            loadMenuSettings();

            toggleButton.setLayoutX(SIDEBAR_LEFT_ANCHOR + EXPANDED_WIDTH);
            toggleButton.toFront();

            modal = new ModalContainer();
            modalContainer.getChildren().add(modal);

            modalContainer.setPickOnBounds(false);
            modalContainer.toFront();

            categoriesList.addEventHandler(DraggableCategoryItem.CategoryReorderEvent.CATEGORY_REORDERED, this::updateCategoryDisplayOrder);
        }

        private void loadCategories() {
            List<MenuCategory> categoryList = menuCategoryService.getAll(false);

            categoryList.sort(Comparator.comparingInt(MenuCategory::getDisplayOrder));

            categories = FXCollections.observableArrayList(categoryList);
            populateCategoriesList();
        }

        private void populateCategoriesList() {
            categoriesList.getChildren().clear();

            for (MenuCategory category : categories) {
                DraggableCategoryItem categoryItem = new DraggableCategoryItem(category.getName());

                categoryItem.setOnMouseClicked(event -> {
                    if (categoryItem.getStyleClass().contains("selected")) {
                        categoryItem.setSelected(false);
                        selectedCategory = null;
                        filterMenuItemsByCategory(null);
                    } else {
                        categoriesList.getChildren().forEach(node -> {
                            if (node instanceof DraggableCategoryItem) {
                                ((DraggableCategoryItem) node).setSelected(false);
                            }
                        });
                        categoryItem.setSelected(true);
                        selectedCategory = category;
                        filterMenuItemsByCategory(category);
                    }
                });

                categoriesList.getChildren().add(categoryItem);
            }
        }

        private void updateCategoryDisplayOrder(DraggableCategoryItem.CategoryReorderEvent event) {
            for (int i = 0; i < categoriesList.getChildren().size(); i++) {
                Node node = categoriesList.getChildren().get(i);

                if (node instanceof DraggableCategoryItem) {
                    DraggableCategoryItem categoryItem = (DraggableCategoryItem) node;

                    MenuCategory category = categories.stream()
                            .filter(c -> c.getName().equals(categoryItem.getCategoryLabel().getText()))
                            .findFirst()
                            .orElse(null);

                    if (category != null) {
                        category.setDisplayOrder(i);

                        menuCategoryService.update(category.getId(), convertToDTO(category));
                    }
                }
            }
        }

        private MenuCategoryDTO convertToDTO(MenuCategory category) {
            MenuCategoryDTO dto = new MenuCategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDisplayOrder(category.getDisplayOrder());
            return dto;
        }

        private void filterMenuItemsByCategory(MenuCategory category) {
            menuItemFlowPane.getChildren().clear();

            if (category == null) {
                for (MenuItem menuItem : menuItems) {
                    Pane menuItemCard = createMenuItemCard(menuItem);
                    menuItemFlowPane.getChildren().add(menuItemCard);
                }
            } else {
                for (MenuItem menuItem : menuItems) {
                    if (menuItem.getCategory().getId().equals(category.getId())) {
                        Pane menuItemCard = createMenuItemCard(menuItem);
                        menuItemFlowPane.getChildren().add(menuItemCard);
                    }
                }
            }
        }


        private void loadMenuItems() {
            List<MenuItem> itemList = menuItemService.getAll(false);
            menuItems = FXCollections.observableArrayList(itemList);
            filterMenuItemsByCategory(selectedCategory);
        }

        private void loadMenuSettings() {
            List<MenuHeader> headers = menuHeaderService.getAll(false);
            List<MenuFooter> footers = menuFooterService.getAll(false);

            currentMenuHeader = headers.isEmpty() ? null : headers.get(headers.size() - 1);
            currentMenuFooter = footers.isEmpty() ? null : footers.get(footers.size() - 1);

            menuHeader = currentMenuHeader != null ? currentMenuHeader.getHeader() : "";
            menuFooter = currentMenuFooter != null ? currentMenuFooter.getFooter() : "";
        }

        private void populateMenuItemFlowPane() {
            menuItemFlowPane.getChildren().clear();
            for (MenuItem menuItem : menuItems) {
                Pane menuItemCard = createMenuItemCard(menuItem);
                menuItemFlowPane.getChildren().add(menuItemCard);
            }
        }

        private Pane createMenuItemCard(MenuItem menuItem) {
            Pane card = new Pane();
            card.getStyleClass().add("menu-item-card");

            Label nameLabel = new Label(menuItem.getName());
            nameLabel.getStyleClass().add("name");
            nameLabel.setLayoutX(15);
            nameLabel.setLayoutY(15);

            Label descriptionLabel = new Label(menuItem.getDescription());
            descriptionLabel.getStyleClass().add("description");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setPrefWidth(370);
            descriptionLabel.setLayoutX(15);
            descriptionLabel.setLayoutY(45);

            Label bottomCategoryLabel = new Label(menuItem.getCategory().getName());
            bottomCategoryLabel.getStyleClass().add("category");
            bottomCategoryLabel.setLayoutX(320);
            bottomCategoryLabel.setLayoutY(90);

            Label priceLabel = new Label(menuItem.getPrice().compareTo(BigDecimal.ZERO) > 0 ? String.format("R$%.2f", menuItem.getPrice()) : "");
            priceLabel.getStyleClass().add("price");
            priceLabel.setLayoutX(15);
            priceLabel.setLayoutY(90);

            Label emojiLabel = new Label(menuItem.getEmoji());
            emojiLabel.getStyleClass().add("emoji");
            emojiLabel.setLayoutX(50);
            emojiLabel.setLayoutY(90);

            Button editButton = new Button("\uF88D");
            editButton.getStyleClass().add("action-button");
            editButton.setLayoutX(320);
            editButton.setLayoutY(10);
            editButton.setOnAction(e -> showEditItemDialog(menuItem));

            Button deleteButton = new Button("\uE872");
            deleteButton.getStyleClass().add("action-button");
            deleteButton.setLayoutX(360);
            deleteButton.setLayoutY(10);
            deleteButton.setOnAction(e -> showDeleteConfirmationDialog(menuItem));

            card.getChildren().addAll(nameLabel, descriptionLabel, bottomCategoryLabel, priceLabel, emojiLabel, editButton, deleteButton);

            return card;
        }

        @FXML
        private void showAddCategoryDialog(ActionEvent event) {
            CategoryModalForm form = new CategoryModalForm(
                    () -> modal.hide(),
                    categoryDTO -> {
                        MenuCategory savedCategory = menuCategoryService.create(categoryDTO);
                        categories.add(savedCategory);
                        refreshCategories(savedCategory);
                        modal.hide();
                    }
            );

            modal.show(form);
        }

        @FXML
        private void showAddItemDialog(ActionEvent event) {
            MenuItemModalForm form = new MenuItemModalForm(
                    categories,
                    () -> modal.hide(),
                    itemDTO -> {
                        MenuItem newItem = menuItemService.create(itemDTO);
                        menuItems.add(newItem);
                        populateMenuItemFlowPane();
                        modal.hide();
                    }
            );

            modal.show(form);
        }

        private void showEditItemDialog(MenuItem menuItem) {
            MenuItemModalForm form = new MenuItemModalForm(
                    categories,
                    () -> modal.hide(),
                    itemDTO -> {
                        MenuItem updatedItem = menuItemService.update(itemDTO.getId(), itemDTO);
                        int index = menuItems.indexOf(menuItem);
                        if (index >= 0) {
                            menuItems.set(index, updatedItem);
                        }
                        populateMenuItemFlowPane();
                        modal.hide();
                    },
                    menuItem
            );

            modal.show(form);
        }

        private void showDeleteConfirmationDialog(MenuItem menuItem) {
            String message = String.format("Do you want to delete \"%s\"?", menuItem.getName());

            DeleteConfirmationModal confirmationModal = new DeleteConfirmationModal(
                    message,
                    () -> modal.hide(),
                    () -> {
                        menuItemService.delete(menuItem.getId());
                        menuItems.remove(menuItem);
                        populateMenuItemFlowPane();
                        modal.hide();
                    }
            );

            modal.show(confirmationModal);
        }

        @FXML
        private void refreshCategories(MenuCategory newCategory) {
            DraggableCategoryItem categoryItem = new DraggableCategoryItem(newCategory.getName());

            categoryItem.setOnMouseClicked(event -> {
                if (categoryItem.getStyleClass().contains("selected")) {
                    categoryItem.setSelected(false);
                    selectedCategory = null;
                    filterMenuItemsByCategory(null);
                } else {
                    categoriesList.getChildren().forEach(node -> {
                        if (node instanceof DraggableCategoryItem) {
                            ((DraggableCategoryItem) node).setSelected(false);
                        }
                    });
                    categoryItem.setSelected(true);
                    selectedCategory = newCategory;
                    filterMenuItemsByCategory(newCategory);
                }
            });

            categoriesList.getChildren().add(categoryItem);
        }

        @FXML
        private void toggleSidebar() {
            double targetWidth = isSidebarExpanded ? COLLAPSED_WIDTH : EXPANDED_WIDTH;

            double targetButtonX = isSidebarExpanded ?
                    SIDEBAR_LEFT_ANCHOR - 5 :
                    SIDEBAR_LEFT_ANCHOR + EXPANDED_WIDTH;

            Timeline timeline = new Timeline();

            KeyValue sidebarWidth = new KeyValue(
                    sidebar.prefWidthProperty(),
                    targetWidth,
                    javafx.animation.Interpolator.EASE_BOTH
            );

            KeyValue buttonX = new KeyValue(
                    toggleButton.layoutXProperty(),
                    targetButtonX,
                    javafx.animation.Interpolator.EASE_BOTH
            );

            double targetMainX = isSidebarExpanded ?
                    SIDEBAR_LEFT_ANCHOR + COLLAPSED_WIDTH + 15 :
                    SIDEBAR_LEFT_ANCHOR + EXPANDED_WIDTH + 15;
            KeyValue mainContentX = new KeyValue(
                    menuItemFlowPane.layoutXProperty(),
                    targetMainX,
                    javafx.animation.Interpolator.EASE_BOTH
            );

            KeyValue headerOpacity = new KeyValue(
                    sidebarHeader.opacityProperty(),
                    isSidebarExpanded ? 0 : 1,
                    javafx.animation.Interpolator.EASE_BOTH
            );
            KeyValue categoriesOpacity = new KeyValue(
                    categoryScroll.opacityProperty(),
                    isSidebarExpanded ? 0 : 1,
                    javafx.animation.Interpolator.EASE_BOTH
            );

            timeline.setOnFinished(event -> {
                toggleButton.setText(isSidebarExpanded ? "\uE5CC" : "\uE5CB");
                if (isSidebarExpanded) {
                    sidebarHeader.setDisable(true);
                    sidebarHeader.setVisible(false);
                    categoryScroll.setDisable(true);
                    categoryScroll.setVisible(false);
                    AnchorPane.setLeftAnchor(translateVbox, 205.00);
                } else {
                    sidebarHeader.setDisable(false);
                    sidebarHeader.setVisible(true);
                    categoryScroll.setDisable(false);
                    categoryScroll.setVisible(true);
                    AnchorPane.setLeftAnchor(translateVbox, 460.0);
                }
                isSidebarExpanded = !isSidebarExpanded;
            });

            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(ANIMATION_DURATION),
                    sidebarWidth,
                    buttonX,
                    mainContentX,
                    headerOpacity,
                    categoriesOpacity
            );

            timeline.getKeyFrames().add(keyFrame);

            if (!isSidebarExpanded) {
                sidebarHeader.setVisible(true);
                categoryScroll.setVisible(true);
            }

            timeline.play();
        }

        @FXML
        private void showGenerateMenuDialog() {
            MenuGenerationModal form = new MenuGenerationModal(
                    menuItems,
                    () -> modal.hide(),
                    () -> {
                        List<MenuItem> selectedItems = ((MenuGenerationModal) modal
                                .getChildren()
                                .get(0)
                                .lookup(".modal-form"))
                                .getSelectedItems();

                        if (selectedItems.isEmpty()) {
                            return;
                        }

                        showGeneratedMenuModal(getFormattedMenu(selectedItems), selectedItems);
                    },
                    (menuItem) -> {
                        menuItemService.updateMenuItemFavoriteStatus(menuItem.getId(), menuItem.isFavorite());
                    }
            );

            modal.show(form);
        }

        private void showGeneratedMenuModal(String menuContent, List<MenuItem> menuItems) {
            GeneratedMenuModal form = new GeneratedMenuModal(
                    menuContent,
                    menuItems,
                    () -> modal.hide()
            );

            modal.show(form);
        }

        @FXML
        private void showMenuSettingsDialog() {
            MenuSettingsModal form = new MenuSettingsModal(
                    menuHeader,
                    menuFooter,
                    () -> modal.hide(),
                    () -> {
                        MenuSettingsModal settingsModal = (MenuSettingsModal) modal
                                .getChildren()
                                .get(0)
                                .lookup(".modal-form");

                        String newHeader = settingsModal.getHeaderText();
                        String newFooter = settingsModal.getFooterText();

                        if (!newHeader.equals(menuHeader)) {
                            MenuHeaderDTO headerDTO = new MenuHeaderDTO();
                            headerDTO.setHeader(newHeader);
                            if (currentMenuHeader != null) {
                                headerDTO.setId(currentMenuHeader.getId());
                                currentMenuHeader = menuHeaderService.update(currentMenuHeader.getId(), headerDTO);
                            } else {
                                currentMenuHeader = menuHeaderService.create(headerDTO);
                            }
                            menuHeader = newHeader;
                        }

                        if (!newFooter.equals(menuFooter)) {
                            MenuFooterDTO footerDTO = new MenuFooterDTO();
                            footerDTO.setFooter(newFooter);
                            if (currentMenuFooter != null) {
                                footerDTO.setId(currentMenuFooter.getId());
                                currentMenuFooter = menuFooterService.update(currentMenuFooter.getId(), footerDTO);
                            } else {
                                currentMenuFooter = menuFooterService.create(footerDTO);
                            }
                            menuFooter = newFooter;
                        }

                        modal.hide();
                    }
            );

            modal.show(form);
        }

            private String getFormattedMenu(List<MenuItem> menuItems) {
                StringBuilder menuText = new StringBuilder();

                if (menuHeader != null && !menuHeader.trim().isEmpty()) {
                    menuText.append(menuHeader).append("\n\n");
                }

                Map<MenuCategory, List<MenuItem>> itemsByCategory = new LinkedHashMap<>();

                for (MenuItem item : menuItems) {
                    itemsByCategory.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
                }

                List<Map.Entry<MenuCategory, List<MenuItem>>> sortedCategories = itemsByCategory.entrySet()
                        .stream()
                        .sorted((a, b) -> {
                            Integer orderA = a.getKey().getDisplayOrder() != null ? a.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                            Integer orderB = b.getKey().getDisplayOrder() != null ? b.getKey().getDisplayOrder() : Integer.MAX_VALUE;
                            return orderA.compareTo(orderB);
                        })
                        .collect(Collectors.toList());

                for (Map.Entry<MenuCategory, List<MenuItem>> entry : sortedCategories) {
                    List<MenuItem> items = entry.getValue();

                    for (MenuItem item : items) {
                        String emoji = item.getEmoji() != null ? item.getEmoji() : "•";
                        menuText.append(emoji).append(item.getName()).append("\n");
                    }

                    menuText.append("\n");
                }

                if (menuFooter != null && !menuFooter.trim().isEmpty()) {
                    menuText.append(menuFooter);
                }

                return menuText.toString();
            }

        private void filterMenuItems(String searchText) {
            if (searchText == null || searchText.trim().isEmpty()) {
                populateMenuItemFlowPane();
                return;
            }

            String lowerCaseSearchText = searchText.toLowerCase().trim();

            menuItemFlowPane.getChildren().clear();

            for (MenuItem menuItem : menuItems) {
                boolean matches =
                        menuItem.getName().toLowerCase().contains(lowerCaseSearchText) ||
                                menuItem.getDescription().toLowerCase().contains(lowerCaseSearchText) ||
                                menuItem.getCategory().getName().toLowerCase().contains(lowerCaseSearchText);

                if (matches) {
                    Pane menuItemCard = createMenuItemCard(menuItem);
                    menuItemFlowPane.getChildren().add(menuItemCard);
                }
            }
        }
    }