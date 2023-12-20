package com.example.finalproject;

// Interface defining the contract for machine learning models
public interface MachineLearningModel {

    // Load a machine learning model from the specified path and with the given name
    void loadModel(String modelPath, String modelName);

    // Unload the currently loaded machine learning model
    void unloadModel();

    // Get the label result based on the provided data
    int getLabelResult(String data);

    // Get information about the loaded machine learning model
    String getModelInfo();

    // Add an observer to receive updates on recognition results
    void addObserver(RecognitionResultObserver observer);

    // Remove an observer from the list of observers
    void removeObserver(RecognitionResultObserver observer);

    // Notify all registered observers with the provided recognition result
    void notifyObservers(int result);

}

