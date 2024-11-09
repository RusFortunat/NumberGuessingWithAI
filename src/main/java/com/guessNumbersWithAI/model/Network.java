package com.guessNumbersWithAI.model;
// No training will be done here. The neural network parameters will be simply loaded from the database
// I will provide two options to the user: to load parameters of NN that I've trained myself in my other
// project, or to load parameters that have been obtained from state-of-art PyTorch training method

import java.util.ArrayList;
import java.util.List;

public class Network {

    public String chosenModel;
    public String testField;
    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private ArrayList<Double> rawInput; // will be provided by user
    private double[] inputVector; // will be provided by user
    private double[] hiddenVector; // null at the beginning
    private double[] outputVector;
    // the neural network parameters that will be loaded from the file
    private double[][] firstLayerWeights;
    private double[] firstLayerBiases;
    private double[][] secondLayerWeights;
    private double[] secondLayerBiases;

    // I will let the user load the parameters from the MySQL DB
    public Network() {
        this.testField = "";
        this.chosenModel = "";
        this.inputSize = 28*28;
        this.hiddenSize = 128;
        this.outputSize = 10;
        this.rawInput = new ArrayList<>(); // input that takes all three RGB channels
        this.inputVector = new double[inputSize]; // we will convert raw input to grayscale image
        this.hiddenVector = new double[hiddenSize];
        this.outputVector = new double[outputSize];
    }

    // loads the parameters of the chosen network
    public void loadNetworkParameters(){

    }

    // Forward propagation: the neural network gets user image input, that is converted to the input vector
    // and returns as an output the probabilities for different numbers
    public double[] forward(double[] input){
        // forward propagation is not that hard, just do the following:
        // 1. Take matrix product and add biases [z] = [firstLayerWeights]*[input] + [firstLayerBiases];
        // 2. Obtain hidden activation values [y] by applying to [z] some activation function f([z]), i.e., mapping.
        //    Here we use ReLU activation / mapping: f(z) > 0 ? z : 0;
        // 3. Repeat 1.-2. for the next layer with secondLayerWeights and secondLayerBiases to get the [output] vector.

        // compute hidden activation values
        for(int i = 0; i < hiddenSize; i++){
            double sum = 0;
            for(int j = 0; j < inputSize; j++){
                double activation = firstLayerWeights[i][j]*input[j] + firstLayerBiases[i];
                if(activation > 0) sum+= activation; // ReLU activation
            }
            hiddenVector[i] = sum;
        }
        // compute output activations
        double totalSum = 0.0;
        for(int i = 0; i < outputSize; i++){
            double sum = 0;
            for(int j = 0; j < hiddenSize; j++){
                double activation = secondLayerWeights[i][j]*hiddenVector[j] + secondLayerBiases[i];
                if(activation > 0) sum+= activation; // ReLU activation
            }
            outputVector[i] = sum;
            totalSum += Math.exp(sum);
        }

        // normalize the output vector using the SoftMax approach
        for(int i = 0; i < outputSize; i++){
            outputVector[i] = Math.exp(outputVector[i]) / totalSum;
        }

        return outputVector;
    }


    // getters

    public String getChosenModel() {
        return chosenModel;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public ArrayList<Double> getRawInput() {
        return rawInput;
    }

    public double[] getInputVector() {
        return inputVector;
    }

    public double[] getHiddenVector() {
        return hiddenVector;
    }

    public double[] getOutputVector() {
        return outputVector;
    }

    public double[][] getFirstLayerWeights() {
        return firstLayerWeights;
    }

    public double[] getFirstLayerBiases() {
        return firstLayerBiases;
    }

    public double[][] getSecondLayerWeights() {
        return secondLayerWeights;
    }

    public double[] getSecondLayerBiases() {
        return secondLayerBiases;
    }


    // setters


    public void setChosenModel(String chosenModel) {
        this.chosenModel = chosenModel;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public void setHiddenSize(int hiddenSize) {
        this.hiddenSize = hiddenSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public void setRawInput(ArrayList<Double> rawInput) {
        this.rawInput = rawInput;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }
}
