package com.example.finalproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NumberRecognizerApplication extends javafx.application.Application {
    // Entry point for the application, responsible for launching the application
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file and create the root node of the UI
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("Number Recognizer");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    // The main method, serving as the entry point for the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}