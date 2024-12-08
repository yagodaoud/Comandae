package com.yagodaoud.comandae.controller.ui.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class PDFViewerModal {

    public static VBox createPDFPreview(String pdfPath) {
        VBox pdfPreviewBox = new VBox();

        try {
            PDDocument document = Loader.loadPDF(new File(pdfPath));

            if (document.getNumberOfPages() < 1) {
                System.out.println("Failed to render the PDF page as an image.");
            }

            PDFRenderer pdfRenderer = new PDFRenderer(document);

            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);

            if (bufferedImage == null) {
                System.out.println("No pages found in the PDF.");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

            Image image = new Image(inputStream);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(600);
            imageView.setPreserveRatio(true);

            pdfPreviewBox.getChildren().add(imageView);

            baos.close();

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while loading the PDF.");
        }

        return pdfPreviewBox;
    }
}