package com.yagodaoud.comandae.controller.ui.component;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class DraggableCategoryItem extends HBox {
    private double mouseX;
    private double mouseY;
    private final Label categoryLabel;
    private final Label dragIcon;
    private boolean isDragging = false;
    private final DropShadow dragShadow;
    private double startY;
    private VBox parent;
    private int originalIndex;
    private double itemHeight;
    private List<Node> initialOrder;

    public DraggableCategoryItem(String categoryName) {
        super(10);
        getStyleClass().add("category-item");
        setAlignment(Pos.CENTER_LEFT);

        dragIcon = new Label("\uE945");
        dragIcon.getStyleClass().add("drag-icon");
        dragIcon.setCursor(Cursor.MOVE);

        categoryLabel = new Label(categoryName);
        categoryLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(categoryLabel, javafx.scene.layout.Priority.ALWAYS);

        dragShadow = new DropShadow();
        dragShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        dragShadow.setRadius(10);
        dragShadow.setOffsetY(5);
        dragShadow.setSpread(0.2);

        getChildren().addAll(dragIcon, categoryLabel);
        setupDragHandlers();
    }

    private void setupDragHandlers() {
        dragIcon.setOnMousePressed(this::handleMousePressed);
        dragIcon.setOnMouseDragged(this::handleMouseDragged);
        dragIcon.setOnMouseReleased(this::handleMouseReleased);

        dragIcon.setOnMouseEntered(e -> {
            if (!isDragging) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), dragIcon);
                scaleTransition.setToX(1.2);
                scaleTransition.setToY(1.2);
                scaleTransition.play();
            }
        });

        dragIcon.setOnMouseExited(e -> {
            if (!isDragging) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), dragIcon);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.play();
            }
        });
    }

    private void handleMousePressed(MouseEvent event) {
        parent = (VBox) getParent();
        originalIndex = parent.getChildren().indexOf(this);

        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
        startY = getTranslateY();
        isDragging = true;

        initialOrder = new ArrayList<>(parent.getChildren());
        itemHeight = getBoundsInParent().getHeight() + parent.getSpacing();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), this);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.play();

        setEffect(dragShadow);
        setViewOrder(-1);
        setCursor(Cursor.MOVE);
        getStyleClass().add("dragging");

        event.consume();
    }

    private void handleMouseDragged(MouseEvent event) {
        if (!isDragging) return;

        double offsetY = event.getSceneY() - mouseY;
        setTranslateY(startY + offsetY);

        Point2D localPoint = parent.sceneToLocal(event.getSceneX(), event.getSceneY());
        int newIndex = calculateNewIndex(localPoint.getY());

        if (newIndex != originalIndex && newIndex >= 0 && newIndex < parent.getChildren().size()) {
            for (Node child : parent.getChildren()) {
                if (child != this) {
                    int childIndex = parent.getChildren().indexOf(child);

                    double targetY = 0;
                    if (newIndex > originalIndex) {
                        if (childIndex <= newIndex && childIndex > originalIndex) {
                            targetY = -itemHeight;
                        }
                    } else {
                        if (childIndex >= newIndex && childIndex < originalIndex) {
                            targetY = itemHeight;
                        }
                    }

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), child);
                    tt.setToY(targetY);
                    tt.setInterpolator(Interpolator.EASE_BOTH);
                    tt.play();
                }
            }
            originalIndex = newIndex;
        }

        event.consume();
    }

    private void handleMouseReleased(MouseEvent event) {
        if (!isDragging) return;
        isDragging = false;

        Point2D localPoint = parent.sceneToLocal(event.getSceneX(), event.getSceneY());
        int finalIndex = calculateNewIndex(localPoint.getY());

        double currentTranslateY = getTranslateY();

        parent.getChildren().remove(this);
        if (finalIndex >= 0 && finalIndex <= parent.getChildren().size()) {
            parent.getChildren().add(finalIndex, this);
        } else {
            parent.getChildren().add(originalIndex, this);
        }

        setTranslateY(currentTranslateY);

        List<TranslateTransition> transitions = new ArrayList<>();

        for (Node child : parent.getChildren()) {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), child);
            tt.setToY(0);
            tt.setInterpolator(Interpolator.EASE_OUT);
            transitions.add(tt);
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), this);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(transitions);
        parallelTransition.getChildren().add(scaleTransition);

        parallelTransition.setOnFinished(e -> {
            setEffect(null);
            setCursor(Cursor.DEFAULT);
            getStyleClass().remove("dragging");
            setViewOrder(0);
            fireEvent(new CategoryReorderEvent(CategoryReorderEvent.CATEGORY_REORDERED, this));
        });

        parallelTransition.play();
        event.consume();
    }

    private int calculateNewIndex(double y) {
        double itemHeight = getBoundsInParent().getHeight() + parent.getSpacing();
        int index = (int) (y / itemHeight);
        return Math.min(Math.max(index, 0), parent.getChildren().size() - 1);
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            getStyleClass().add("selected");
        } else {
            getStyleClass().remove("selected");
        }
    }

    public static class CategoryReorderEvent extends javafx.event.Event {
        public static final javafx.event.EventType<CategoryReorderEvent> CATEGORY_REORDERED =
                new javafx.event.EventType<>(javafx.event.Event.ANY, "CATEGORY_REORDERED");

        public CategoryReorderEvent(javafx.event.EventType<? extends Event> eventType, DraggableCategoryItem source) {
            super(eventType);
        }
    }
}