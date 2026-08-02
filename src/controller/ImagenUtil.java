package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImagenUtil {
    /**
     * Convierte bytes de la BD a un ImageIcon escalado.
     */
    public static ImageIcon bytesToIcon(byte[] imgBytes, int width, int height) {
        if (imgBytes == null) return null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imgBytes);
            BufferedImage bimg = ImageIO.read(bis);
            // Escalado suave para que no se vea pixelado
            Image scaled = bimg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            System.err.println("Error al procesar imagen: " + e.getMessage());
            return null;
        }
    }
}