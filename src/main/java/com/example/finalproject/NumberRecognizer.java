package com.example.finalproject;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import java.nio.FloatBuffer;
import org.tensorflow.Tensor;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class NumberRecognizer implements MachineLearningModel{
    private List<RecognitionResultObserver> observers = new ArrayList<>();
    public static Session session;
    private static FloatBuffer data; // Encapsulation for private data

    // Constructor to initialize the session
    public NumberRecognizer() {
        loadModelandCreateSession();
    }

    // Utility method to print the contents of a 2D float array
    private static void printFloatArray(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println(); 
        }
    }

    // Load the TensorFlow model and create a session
    private void loadModelandCreateSession() {
        SavedModelBundle modelBundle = SavedModelBundle.load(
                "/Users/chenjiamin/Desktop/NEU/INFO5100/INFO5100_002636593_JiaminChen/Final_Project/saved_model_v2",
                "serve");
        session = modelBundle.session();
        System.out.println("Model loaded, session created!");
    }

    // Retrieve the label result from the loaded model based on the provided image path
    @Override
    public int getLabelResult(String image_path) {
        data = ImageConverter.convertImage2FloatBuffer(image_path);

        // System.out.println("data ready! " + data.capacity());

        float[] keep_prob_array = new float[1024];
        Arrays.fill(keep_prob_array, 1f);

        Tensor<Float> x = Tensor.create(new long[] { 784 }, data);
        Tensor<Float> keep_prob = Tensor.create(new long[] { 1, 1024 }, FloatBuffer.wrap(keep_prob_array));

        float[][] matrix = session.runner()
                .feed("x", x)
                .feed("keep_prob", keep_prob)
                .fetch("y_conv")
                .run()
                .get(0)
                .copyTo(new float[1][10]);

        float maxVal = matrix[0][0];
        printFloatArray(matrix);
        int predict = 0;
        for (int p = 1; p < matrix[0].length; ++p) {
            float val = matrix[0][p];
            if (val > maxVal) {
                predict = p;
                maxVal = val;
            }
        }
        return predict;
    }

    // Load the TensorFlow model from the specified path and with the given name
    @Override
    public void loadModel(String modelPath, String modelName) {
        SavedModelBundle modelBundle = SavedModelBundle.load(modelPath, modelName);
        session = modelBundle.session();
        System.out.println("Model loaded, session created!");
    }

    // Unload the TensorFlow model by closing the session
    @Override
    public void unloadModel() {
        session.close();
        System.out.println("Model unloaded, session closed.");
    }

    // Get information about the loaded model
    @Override
    public String getModelInfo() {
        return "TensorFlow Model Version 1.15.0, Trained on Dataset MNIST";
    }

    // Encapsulation for public accessor and modifier methods for access and modification of private data
    public static FloatBuffer getData() {
        return data;
    }

    public static void setData(FloatBuffer data) {
        NumberRecognizer.data = data;
    }

    @Override
    public void addObserver(RecognitionResultObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RecognitionResultObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(int result) {
        // Notify all observers with the updated recognition result
        for (RecognitionResultObserver observer : observers) {
            observer.updateRecognitionResult(result);
        }
    }

}