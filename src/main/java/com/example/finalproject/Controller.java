package com.example.finalproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller implements RecognitionResultObserver{

    @FXML
    private Canvas handwritingCanvas;
    @FXML
    private GraphicsContext gc;

    @FXML
    private Button recognizeButton;

    @FXML
    private Label resultLabel;

    @FXML private NumberRecognizer numberRecognizer;

    @FXML
    public void initialize() {
        System.out.println("++++++++entering initialize");
        // Initialize the graphics context and set initial properties
        gc = handwritingCanvas.getGraphicsContext2D();
        gc.setLineWidth(14.0);

        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, handwritingCanvas.getWidth(), handwritingCanvas.getHeight());
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);

        // Add event handlers for mouse interactions on the canvas
        handwritingCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onCanvasMousePressed);
        handwritingCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onCanvasMouseDragged);

        System.out.println("++++++++Finishing initialize");

        // Create an instance of NumberRecognizer and add the controller as an observer
        numberRecognizer = new NumberRecognizer();
        numberRecognizer.addObserver(this);

        // Set an action for the recognize button
        recognizeButton.setOnAction(event -> {
            sendToBackendAndShowResult();
        });
    }

    @FXML
    public void onCanvasMouseDragged(MouseEvent event) {
        // Handle mouse dragged event on the canvas
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
    }

    @FXML
    public void onCanvasMousePressed(MouseEvent event) {
        // Handle mouse pressed event on the canvas
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }

    @FXML
    private void clearCanvas() {
        // Clear the content of the canvas
        gc.clearRect(0, 0, handwritingCanvas.getWidth(), handwritingCanvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, handwritingCanvas.getWidth(), handwritingCanvas.getHeight());
    }

    @FXML
    public String saveAsPNG() {
        // Save the content of the canvas as a PNG file
        String filePath = "src/main/resources/com/example/finalproject/images/image.png";

        File file = new File(filePath);

        WritableImage writableImage = new WritableImage((int) handwritingCanvas.getWidth(), (int) handwritingCanvas.getHeight());
        handwritingCanvas.snapshot(null, writableImage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @FXML
    private void sendToBackendAndShowResult() {
        // Process the saved image, get the recognition result, and update the UI
        String imagePath = saveAsPNG();
        if (imagePath != null) {
            int backendResult = numberRecognizer.getLabelResult(imagePath);
            numberRecognizer.notifyObservers(backendResult);
        }
    }

    @Override
    public void updateRecognitionResult(int result) {
        // Update the UI with the recognition result
        Platform.runLater(() -> resultLabel.setText("Recognition Result: " + result));
    }
}
