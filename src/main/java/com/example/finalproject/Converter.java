package com.example.finalproject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;

// Abstract class providing common functionality for image conversion
public abstract class Converter {

    // Load an image from the specified path
    protected BufferedImage loadImage(String imagePath) {
        try {
            InputStream input = new FileInputStream(new File(imagePath));
            BufferedImage originalImage = ImageIO.read(input);
            return originalImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Abstract method for saving images as PNG
    public abstract byte[] saveAsPNG(BufferedImage image);

    // Abstract method for clearing the canvas
    public abstract void clearCanvas(BufferedImage image);
}
