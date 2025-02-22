package com.yagodaoud.comandae.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Locale;

public class PixQRGenerator {

    public static String generatePixCode(String cnpj, BigDecimal amount) {
        String pixDomain = "BR.GOV.BCB.PIX";
        String merchantCategoryCode = "0000";
        String countryCode = "BR";
        String currency = "986";

        StringBuilder payload = new StringBuilder();
        payload.append("00020126");
        payload.append("360014").append(pixDomain);
        payload.append("01").append(String.format("%02d", cnpj.length())).append(cnpj);
        payload.append("5204").append(merchantCategoryCode);
        payload.append("5303").append(currency);

        String amountStr = String.format(Locale.ENGLISH, "%.2f", amount);
        payload.append("54").append(String.format("%02d", amountStr.length())).append(amountStr);

        payload.append("5802").append(countryCode);

        payload.append("5901N");
        payload.append("6001C");
        payload.append("62070503***");

        payload.append("6304");

        String crc = crc16(payload.toString());
        return payload.toString() + crc;
    }

    public static Image generateQRCodeImage(String pixCode, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(pixCode, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return convertToFxImage(bufferedImage);
    }

    private static Image convertToFxImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }

        return writableImage;
    }

    private static String crc16(String payload) {
        int polynomial = 0x1021;
        int crc = 0xFFFF;

        byte[] bytes = payload.getBytes();
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }
        crc &= 0xFFFF;

        return String.format("%04X", crc);
    }
}