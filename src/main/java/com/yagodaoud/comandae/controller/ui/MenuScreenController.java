package com.yagodaoud.comandae.controller.ui;

import com.yagodaoud.comandae.controller.ui.component.CategoryModalForm;
import com.yagodaoud.comandae.controller.ui.component.DraggableCategoryItem;
import com.yagodaoud.comandae.controller.ui.component.MenuItemModalForm;
import com.yagodaoud.comandae.controller.ui.component.ModalContainer;
import com.yagodaoud.comandae.model.NavigationScreen;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.service.menu.MenuCategoryService;
import com.yagodaoud.comandae.service.menu.MenuItemService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class MenuScreenController {

    @FXML
    @Autowired
    private SidebarController sidebarController;

    @FXML private FlowPane menuItemFlowPane;

    @FXML
    private VBox sidebar;
    @FXML private StackPane sidebarHeader;
    @FXML private ScrollPane categoryScroll;

    @FXML
    private Button toggleButton;

    private boolean isSidebarExpanded = true;

    private static final double EXPANDED_WIDTH = 240;
    private static final double COLLAPSED_WIDTH = 0;
    private static final double ANIMATION_DURATION = 170;
    private static final double SIDEBAR_LEFT_ANCHOR = 205;

    @FXML
    private VBox categoriesList;

    private ObservableList<MenuCategory> categories;
    private ObservableList<MenuItem> menuItems;

    @Autowired
    private MenuCategoryService menuCategoryService;

    @Autowired
    private MenuItemService menuItemService;

    @FXML
    private StackPane modalContainer;
    private ModalContainer modal;

    @FXML
    private void initialize() { System.out.println(Font.getFamilies());

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

        sidebarHeader.maxWidthProperty().bind(sidebar.prefWidthProperty());

        sidebarController.setSelectedScreen(NavigationScreen.MENU);
        loadCategories();
        loadMenuItems();

        toggleButton.setLayoutX(SIDEBAR_LEFT_ANCHOR + EXPANDED_WIDTH);
        toggleButton.toFront();

        modal = new ModalContainer();
        modalContainer.getChildren().add(modal);

        modalContainer.setPickOnBounds(false);
        modalContainer.toFront();
    }

    private void loadCategories() {
        List<MenuCategory> categoryList = menuCategoryService.getAll(false);
        categories = FXCollections.observableArrayList(categoryList);
        populateCategoriesList();
    }

    private void populateCategoriesList() {
        categoriesList.getChildren().clear();
        for (MenuCategory category : categories) {
            DraggableCategoryItem categoryItem = new DraggableCategoryItem(category.getName());

            categoryItem.setOnMouseClicked(event -> {
                categoriesList.getChildren().forEach(node -> {
                    if (node instanceof DraggableCategoryItem) {
                        ((DraggableCategoryItem) node).setSelected(false);
                    }
                });
                categoryItem.setSelected(true);
                // filterMenuItemsByCategory(category);
            });

            categoriesList.getChildren().add(categoryItem);
        }
    }

    private void loadMenuItems() {
        List<MenuItem> itemList = menuItemService.getAll(false);
        menuItems = FXCollections.observableArrayList(itemList);
        populateMenuItemFlowPane();
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

        Label emojiLabel = new Label(menuItem.getEmoji());
        emojiLabel.getStyleClass().add("emoji");

        Label categoryLabel = new Label(menuItem.getCategory().getName());
        categoryLabel.getStyleClass().add("category");

        VBox cardContent = new VBox(nameLabel, emojiLabel, categoryLabel);
        cardContent.setSpacing(5);

        card.getChildren().add(cardContent);
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

    @FXML
    private void refreshCategories(MenuCategory newCategory) {
        DraggableCategoryItem categoryItem = new DraggableCategoryItem(newCategory.getName());

        categoryItem.setOnMouseClicked(event -> {
            categoriesList.getChildren().forEach(node -> {
                if (node instanceof DraggableCategoryItem) {
                    ((DraggableCategoryItem) node).setSelected(false);
                }
            });
            categoryItem.setSelected(true);
            // filterMenuItemsByCategory(newCategory);
        });

        categoriesList.getChildren().add(categoryItem);
    }

    @FXML
    private void toggleSidebar()        {
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
            } else {
                sidebarHeader.setDisable(false);
                sidebarHeader.setVisible(true);
                categoryScroll.setDisable(false);
                categoryScroll.setVisible(true);
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
}