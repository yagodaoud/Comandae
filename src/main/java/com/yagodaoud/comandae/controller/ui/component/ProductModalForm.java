package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.model.Product;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Base64;

public class ProductModalForm extends GridPane {
    private final TextField nameField;
    private final TextField priceField;
    private final ComboBox<Category> categoryComboBox;
    private final Button saveButton;
    private final Button cancelButton;
    private final ImageView imagePreview;
    private final Button uploadButton;
    private final Button removeButton;
    private String base64Image;
    private Long editingId;

    private static final String DEFAULT_IMAGE_PATH = "/images/default-image.png";

    public ProductModalForm(java.util.List<Category> categories,
                            Runnable onCancel,
                            java.util.function.Consumer<ProductDTO> onSave,
                            Product existingProduct) {
        setHgap(10);
        setVgap(15);
        getStyleClass().add("modal-form");

        Text title = new Text(existingProduct == null ? "Add Product" : "Edit Product");
        title.getStyleClass().add("modal-title");
        add(title, 0, 0, 2, 1);

        VBox imageSection = new VBox(10);
        imageSection.setAlignment(Pos.CENTER);

        imagePreview = new ImageView();
        imagePreview.setFitHeight(150);
        imagePreview.setFitWidth(150);
        imagePreview.setPreserveRatio(true);

        uploadButton = new Button("Upload Image");
        uploadButton.setOnAction(e -> handleImageUpload());

        removeButton = new Button("Remove Image");
        removeButton.setOnAction(e -> handleImageRemove());

        imageSection.getChildren().addAll(imagePreview, uploadButton, removeButton);
        add(imageSection, 0, 1, 2, 1);

        nameField = new TextField();
        nameField.setPromptText("Product name");
        addFormRow("Name:", nameField, 2);

        priceField = new TextField();
        priceField.setPromptText("0.00");
        addFormRow("Price:", priceField, 3);

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(javafx.collections.FXCollections.observableArrayList(categories));
        categoryComboBox.setPromptText("Select category");
        categoryComboBox.setCellFactory(lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty ? "" : category.getName());
            }
        });
        categoryComboBox.setButtonCell(categoryComboBox.getCellFactory().call(null));
        addFormRow("Category:", categoryComboBox, 4);

        if (existingProduct != null) {
            editingId = existingProduct.getId();
            nameField.setText(existingProduct.getName());
            priceField.setText(existingProduct.getPrice().toString());
            Category matchingCategory = categories.stream()
                    .filter(c -> c.getId().equals(existingProduct.getCategory().getId()))
                    .findFirst()
                    .orElse(null);
            categoryComboBox.setValue(matchingCategory);

            if (existingProduct.getImage() != null) {
                base64Image = existingProduct.getImage();
                displayBase64Image(base64Image);
            } else {
                displayDefaultImage();
            }
        } else {
            displayDefaultImage();
        }

        saveButton = new Button(existingProduct == null ? "Save" : "Update");
        saveButton.getStyleClass().addAll("button", "add-button");
        saveButton.setOnAction(e -> {
            try {
                ProductDTO product = new ProductDTO();
                product.setName(nameField.getText());
                product.setPrice(new BigDecimal(priceField.getText()));
                product.setCategory(categoryComboBox.getValue());
                product.setImage(base64Image);

                if (editingId != null) {
                    product.setId(editingId);
                }
                onSave.accept(product);
            } catch (NumberFormatException ignored) {
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("button", "cancel-button");
        cancelButton.setOnAction(e -> onCancel.run());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        add(buttonBox, 0, 5, 2, 1);

        setupValidation();
    }

    public ProductModalForm(java.util.List<Category> categories,
                            Runnable onCancel,
                            java.util.function.Consumer<ProductDTO> onSave) {
        this(categories, onCancel, onSave, null);
    }

    private void setupValidation() {
        saveButton.setDisable(true);

        javafx.beans.binding.BooleanBinding isValid = nameField.textProperty().isEmpty()
                .or(priceField.textProperty().isEmpty())
                .or(categoryComboBox.valueProperty().isNull());

        saveButton.disableProperty().bind(isValid);

        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                priceField.setText(oldVal);
            }
        });
    }

    private void addFormRow(String label, Control field, int row) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("form-label");
        add(labelNode, 0, row);
        add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            try {
                byte[] fileContent = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileContent);
                fileInputStream.close();

                base64Image = Base64.getEncoder().encodeToString(fileContent);

                displayBase64Image(base64Image);
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Image Upload Error");
                alert.setContentText("Failed to process the image file.");
                alert.showAndWait();
            }
        }
    }

    private void displayBase64Image(String base64String) {
        try {
            byte[] imageData = Base64.getDecoder().decode(base64String);
            Image image = new Image(new ByteArrayInputStream(imageData));
            imagePreview.setImage(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayDefaultImage() {
        imagePreview.setImage(new Image(DEFAULT_IMAGE_PATH));
        base64Image = null;
    }

    private void handleImageRemove() {
        displayDefaultImage();
    }
}
