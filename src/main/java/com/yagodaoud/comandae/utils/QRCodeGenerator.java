package com.yagodaoud.comandae.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator {
    public static void generateQRCode(String data, String filePath) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(
                data, BarcodeFormat.QR_CODE, 300, 300);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ImageIO.write(image, "png", new File(filePath));
    }
}
