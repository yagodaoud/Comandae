package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.model.menu.MenuItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuPDFGenerator {
    private static final float PAGE_WIDTH = 595.0f; // A4 page width
    private static final float PAGE_HEIGHT = 842.0f; // A4 page height
    private static final float QUADRANT_WIDTH = PAGE_WIDTH / 2;
    private static final float QUADRANT_HEIGHT = PAGE_HEIGHT / 2;
    private static final float MARGIN = 30.0f;

    public static void generateMenuPDF(List<MenuItem> menuItems, String outputFilePath) {
        Map<String, List<MenuItem>> categories = categorizeMenuItems(menuItems);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT));
            document.addPage(page);

            drawQuadrants(document, page);

            populateQuadrant(document, page, categories, 0, 0);
            populateQuadrant(document, page, categories, 0, 1);
            populateQuadrant(document, page, categories, 1, 0);
            populateQuadrant(document, page, categories, 1, 1);

            document.save(outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void populateQuadrant(PDDocument document, PDPage page, Map<String, List<MenuItem>> categories, int row, int col) {
        float x = col * QUADRANT_WIDTH + MARGIN;
        float y = page.getMediaBox().getHeight() - (row * QUADRANT_HEIGHT + MARGIN);

        y = writeRiceAndBeansCategory(document, page, categories, x, y);
        y = writeCategory(document, page, "Meats", categories.get("meats"), x, y);
        y = writeCategory(document, page, "Sides", categories.get("sides"), x, y);
        writeCategory(document, page, "Salads", categories.get("salads"), x, y);
    }

    private static float writeCategory(PDDocument document, PDPage page, String categoryName, List<MenuItem> items, float x, float y) {
        if (items == null || items.isEmpty()) return y;

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12.0f);

            y -= 14;

            if ("Salads".equals(categoryName)) {
                List<List<MenuItem>> optimalCombinations = findOptimalSaladCombinations(items);

                for (List<MenuItem> combination : optimalCombinations) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x, y);

                    for (int j = 0; j < combination.size(); j++) {
                        MenuItem item = combination.get(j);
                        contentStream.showText(item.getName());

                        if (j < combination.size() - 1) {
                            contentStream.showText("      ");
                        }
                    }

                    contentStream.endText();
                    y -= 14;
                }
            } else {
                for (MenuItem item : items) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText(item.getName());
                    contentStream.endText();
                    y -= 14;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return y - 10;
    }

    private static float writeRiceAndBeansCategory(PDDocument document, PDPage page, Map<String, List<MenuItem>> categories, float x, float y) {
        List<MenuItem> riceItems = categories.get("rice_and_beans").stream()
                .filter(item -> item.getCategory().getName().equals("Arroz"))
                .toList();

        List<MenuItem> beansItems = categories.get("rice_and_beans").stream()
                .filter(item -> item.getCategory().getName().equals("Feijão"))
                .toList();

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12.0f);

            y -= 14;

            if (!riceItems.isEmpty()) {
                contentStream.beginText();
                contentStream.newLineAtOffset(x, y);

                for (int i = 0; i < riceItems.size(); i++) {
                    contentStream.showText(riceItems.get(i).getName());

                    if (i < riceItems.size() - 1) {
                        contentStream.showText(" | ");
                    }
                }

                contentStream.endText();
                y -= 14;
            }

            if (!beansItems.isEmpty()) {
                contentStream.beginText();
                contentStream.newLineAtOffset(x, y);

                for (int i = 0; i < beansItems.size(); i++) {
                    contentStream.showText(beansItems.get(i).getName());

                    if (i < beansItems.size() - 1) {
                        contentStream.showText(" | ");
                    }
                }

                contentStream.endText();
                y -= 14;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return y - 10;
    }


    private static List<List<MenuItem>> findOptimalSaladCombinations(List<MenuItem> items) {
        List<List<MenuItem>> optimalCombinations = new ArrayList<>();

        float maxWidth = QUADRANT_WIDTH - (2 * MARGIN);

        try {
            PDDocument tempDoc = new PDDocument();
            PDPage tempPage = new PDPage();
            tempDoc.addPage(tempPage);

            PDPageContentStream contentStream = new PDPageContentStream(tempDoc, tempPage);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12.0f);

            List<MenuItem> currentLine = new ArrayList<>();
            float currentLineWidth = 0;

            for (MenuItem item : items) {
                float itemWidth = getTextWidth(contentStream, item.getName());
                float spaceWidth = getTextWidth(contentStream, "      ");

                if (!currentLine.isEmpty() &&
                        (currentLineWidth + spaceWidth + itemWidth > maxWidth)) {
                    optimalCombinations.add(new ArrayList<>(currentLine));
                    currentLine.clear();
                    currentLineWidth = 0;
                }

                currentLine.add(item);
                currentLineWidth += (currentLine.size() > 1 ? spaceWidth : 0) + itemWidth;
            }

            if (!currentLine.isEmpty()) {
                optimalCombinations.add(currentLine);
            }

            contentStream.close();
            tempDoc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return optimalCombinations;
    }

    private static float getTextWidth(PDPageContentStream contentStream, String text) throws IOException {
        return new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)
                .getStringWidth(text) / 1000 * 12.0f;
    }

    private static void drawQuadrants(PDDocument document, PDPage page) {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.moveTo(QUADRANT_WIDTH, MARGIN);
            contentStream.lineTo(QUADRANT_WIDTH, PAGE_HEIGHT - MARGIN);
            contentStream.stroke();

            contentStream.moveTo(MARGIN, PAGE_HEIGHT - QUADRANT_HEIGHT);
            contentStream.lineTo(PAGE_WIDTH - MARGIN, PAGE_HEIGHT - QUADRANT_HEIGHT);
            contentStream.stroke();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<MenuItem>> categorizeMenuItems(List<MenuItem> menuItems) {
        Map<String, List<MenuItem>> categories = new HashMap<>();
        categories.put("rice_and_beans", new ArrayList<>());
        categories.put("meats", new ArrayList<>());
        categories.put("sides", new ArrayList<>());
        categories.put("salads", new ArrayList<>());

        for (MenuItem item : menuItems) {
            switch (item.getCategory().getName()) {
                case "Arroz":
                case "Feijão":
                    categories.get("rice_and_beans").add(item);
                    break;
                case "Carnes":
                    categories.get("meats").add(item);
                    break;
                case "Guarnições":
                    categories.get("sides").add(item);
                    break;
                case "Saladas":
                    categories.get("salads").add(item);
                    break;
            }
        }

        return categories;
    }
}