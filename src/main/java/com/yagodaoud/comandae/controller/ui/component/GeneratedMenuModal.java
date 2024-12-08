package com.yagodaoud.comandae.controller.ui.component;

import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.service.MenuPDFGenerator;
import com.yagodaoud.comandae.service.PDFPrinterService;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

public class GeneratedMenuModal extends VBox {
    private final String menuContent;
    private final Runnable onClose;
    private TextArea menuArea;
    private List<MenuItem> menuItemList;
    private VBox originalContent;

    public GeneratedMenuModal(
            String menuContent,
            List<MenuItem> menuItemList,
            Runnable onCloseHandler
    ) {
        this.menuContent = menuContent;
        this.onClose = onCloseHandler;
        this.menuItemList = menuItemList;

        setupUI();
    }

    private void setupUI() {
        getStyleClass().add("modal-form");
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(500);
        setMaxHeight(600);

        Text title = new Text("Generated Menu");
        title.getStyleClass().add("modal-title");

        setupMenuArea();
        HBox buttons = setupButtons();

        originalContent = new VBox(title, menuArea, buttons);
        getChildren().addAll(originalContent);
    }

    private void setupMenuArea() {
        menuArea = new TextArea(menuContent);
        menuArea.setEditable(false);
        menuArea.setWrapText(true);
        menuArea.setPrefRowCount(40);
        menuArea.getStyleClass().add("menu-text-area");
    }

    private HBox setupButtons() {
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setLayoutY(5);

        Button copyButton = new Button("Copy to Clipboard");
        copyButton.getStyleClass().add("primary-button");
        copyButton.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(menuContent);
            Clipboard.getSystemClipboard().setContent(content);

            copyButton.setText("Copied!");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> copyButton.setText("Copy to Clipboard"));
            pause.play();
        });

        Button pdfButton = new Button("PDF");
        pdfButton.getStyleClass().add("primary-button");
        pdfButton.setOnAction(e -> {
            String pdfPath = "files/menu.pdf";

            MenuPDFGenerator.generateMenuPDF(menuItemList, pdfPath);
            showPDFPreview(pdfPath);
        });

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("secondary-button");
        closeButton.setOnAction(e -> onClose.run());

        buttonBox.getChildren().addAll(copyButton, pdfButton, closeButton);
        return buttonBox;
    }

    private void showPDFPreview(String pdfPath) {
        getChildren().clear();

        VBox pdfPreviewBox = PDFViewerModal.createPDFPreview(pdfPath);

        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(e -> {
            getChildren().clear();
            getChildren().addAll(originalContent);
        });

        Button printButton = new Button("Print PDF");
        printButton.getStyleClass().add("primary-button");
        printButton.setOnAction(e -> {
            PDFPrinterService pdfPrinterService = new PDFPrinterService();
            try {
                pdfPrinterService.printPDF(pdfPath);
            } catch (PrinterException | IOException ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, printButton, backButton );
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(printButton, Priority.ALWAYS);

        pdfPreviewBox.getChildren().add(buttonBox);

        VBox.setMargin(buttonBox, new Insets(10, 10, 0, 0));

        getChildren().add(pdfPreviewBox);
    }

    public String getMenuContent() {
        return menuContent;
    }

    public TextArea getMenuArea() {
        return menuArea;
    }
}
