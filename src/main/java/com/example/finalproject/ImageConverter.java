package com.example.finalproject;

import java.io.*;
import java.nio.FloatBuffer;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import javafx.scene.paint.Color;

public class ImageConverter extends Converter{

    // Load and resize the image from the specified path
    private static BufferedImage getImage(String imagePath) {
        try {
            InputStream input = new FileInputStream(new File(imagePath));
            BufferedImage originalImage = ImageIO.read(input);
            BufferedImage resizedImage = resizeImage(originalImage, 28, 28);
            return resizedImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Resize the given image to the specified target width and height
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    // Convert the image to a FloatBuffer containing normalized pixel values
    public static FloatBuffer convertImage2FloatBuffer(String imagePath) {
        BufferedImage grayImage = getImage(imagePath);

        int width = grayImage.getWidth();
        int height = grayImage.getHeight();
        int[] pixels = grayImage.getRGB(0, 0, width, height, null, 0, width);

        // Normalize pixel values to be in the range [0, 1]
        FloatBuffer fb = FloatBuffer.allocate(784);
        for (int pixel : pixels) {
            fb.put((pixel & 0xFF) / 255.0f);
        }
        fb.rewind();
        return fb;
    }

    // Save the provided image as a PNG byte array
    @Override
    public byte[] saveAsPNG(BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // Handle the exception (e.g., log or throw a more specific exception)
            e.printStackTrace();
            return null;
        }
    }

    // Clear the canvas by filling it with a white background
    @Override
    public void clearCanvas(BufferedImage image) {
        // Get the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a Graphics2D object to draw on the image
        Graphics2D g2d = image.createGraphics();

        // Fill the image with a white background
        g2d.setColor(java.awt.Color.white);
        g2d.fillRect(0, 0, width, height);

        // Dispose of the Graphics2D object to free up resources
        g2d.dispose();
    }

}
