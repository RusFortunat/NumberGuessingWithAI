// I will not be training the neural networks here, but will simply upload their parameters from file.
// The trained neural network model was obtained by me writing the whole supervised learning machinery in Java
// from scratch. If you are interested in how I trained these two neural networks, check out this project:
// My implementation from scratch in Java: https://github.com/RusFortunat/java_ML_library

package com.guessNumbersWithAI.model;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

@Component
public class NeuralNetwork {

    private int inputSize = 28*28; // MNIST training images are of 28x28 pixel size
    private int hiddenSize = 256; // arbitrary, should not be too small or too big
    private int outputSize = 10; // 0-9 digits that network will be guessing
    private double[] outputVector;

    // the neural network parameters that will be loaded from the file
    private double[][] firstLayerWeights;
    private double[] firstLayerBiases;
    private double[][] secondLayerWeights;
    private double[] secondLayerBiases;

    // i explicitly define it here so that it can be displayed on the view page
    private int answer;


    // I will let the user to choose which neural network to use and load parameters later
    public NeuralNetwork() {

        this.outputVector = new double[outputSize];

        this.firstLayerWeights = new double[hiddenSize][inputSize];
        this.firstLayerBiases = new double[hiddenSize];
        this.secondLayerWeights = new double[outputSize][hiddenSize];
        this.secondLayerBiases = new double[outputSize];
    }


    // Forward propagation: the neural network gets user image input, that is converted to the input vector
    // and returns as an output the probabilities for different numbers
    public void forward(double[] input) throws RuntimeException{
        // forward propagation is not that hard, just do the following:
        // 1. Take matrix product and add biases [z] = [firstLayerWeights]*[input] + [firstLayerBiases];
        // 2. Obtain hidden activation values [y] by applying to [z] some activation function f([z]), i.e., mapping.
        //    Here we use ReLU activation / mapping: f(z) > 0 ? z : 0;
        // 3. Repeat 1.-2. for the next layer with secondLayerWeights and secondLayerBiases to get the [output] vector.

        double[] hiddenVector = new double[hiddenSize];

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
        double smallestValue = 0.0; // sum of activations can be negative
        for(int i = 0; i < outputSize; i++){
            double sum = 0;
            for(int j = 0; j < hiddenSize; j++){
                double activation = secondLayerWeights[i][j]*hiddenVector[j] + secondLayerBiases[i];
                sum+= activation; // no relu on output values
            }
            outputVector[i] = sum;

            if(sum < smallestValue){
                smallestValue = sum;
            }
        }

        // return the index (which also represents the number) of the output vector with the highest value
        int maxId = 0;
        double maxValue = outputVector[maxId];
        for(int i = 1; i < outputSize; i++){
            if(outputVector[i] > maxValue){
                maxId = i;
                maxValue = outputVector[i];
            }
        }

        // if one of the values is negative, shift the entire vector
        double totalSum = 0;
        for(int i = 0; i < outputSize; i++){
            if(smallestValue < 0) outputVector[i] += Math.abs(smallestValue);
            totalSum += outputVector[i];
        }

        // normalize the output vector
        if(totalSum != 0){
            for(int i = 0; i < outputSize; i++){
                outputVector[i] =outputVector[i] / totalSum;
            }
        }

        // now let's magnify the difference between the output values,
        // so that we can have clear visual effect on html page
        maxValue = outputVector[maxId];
        double highConfidenceValue = 0.9*maxValue;
        double mediumConfidenceValue = 0.5*maxValue;
        double lowConfidenceValue = 0.3*maxValue;
        for(int i = 0; i < outputSize; i++){
            if(outputVector[i] > highConfidenceValue){
                outputVector[i] = Math.min(1.0, 4*outputVector[i]); // opacity is capped at 1.0
            }
            else if(outputVector[i] > mediumConfidenceValue){
                outputVector[i] = Math.min(1.0, 3*outputVector[i]); // opacity is capped at 1.0
            }
            else if(outputVector[i] > lowConfidenceValue){
                outputVector[i] = Math.min(1.0, 3*outputVector[i]); // opacity is capped at 1.0
            }
        }

        answer = maxId;
    }


    // loads the parameters of the chosen network
    public void loadNetworkParameters(InputStream networkParamsFile) throws Exception{

        Scanner reader = new Scanner(networkParamsFile);

        // read the file data
        while(reader.hasNextLine()){

            // skip metadata
            for(int skip = 0; skip < 4; skip++) reader.nextLine();

            // load first layer weights
            String firstLayerWeightsStr = reader.nextLine();
            String[] firstLrWeightsStr = firstLayerWeightsStr.split(",");
            for(int i = 0; i < hiddenSize; i++){
                for(int j = 0; j < inputSize; j++) {
                    int index = i*inputSize + j;
                    this.firstLayerWeights[i][j] = Double.valueOf(firstLrWeightsStr[index]);
                }
            }

            // load first layer biases
            for(int skip = 0; skip < 2; skip++) reader.nextLine();
            String firstLayerBiasesStr = reader.nextLine();
            String[] firstLrBiasesStr = firstLayerBiasesStr.split(",");
            for(int i = 0; i < hiddenSize; i++){
                firstLayerBiases[i] = Double.valueOf(firstLrBiasesStr[i]);
            }

            // load second layer weights
            for(int skip = 0; skip < 2; skip++) reader.nextLine();
            String secondLayerWeightsStr = reader.nextLine();
            String[] secondLrWeightsStr = secondLayerWeightsStr.split(",");
            for(int i = 0; i < outputSize; i++){
                for(int j = 0; j < hiddenSize; j++) {
                    int index = i*hiddenSize + j;
                    this.secondLayerWeights[i][j] = Double.valueOf(secondLrWeightsStr[index]);
                }
            }

            // load first layer biases
            for(int skip = 0; skip < 2; skip++) reader.nextLine();
            String secondLayerBiasesStr = reader.nextLine();
            String[] secondLrBiasesStr = secondLayerBiasesStr.split(",");
            for(int i = 0; i < outputSize; i++){
                secondLayerBiases[i] = Double.valueOf(secondLrBiasesStr[i]);
            }
        }
        reader.close();
    }

    // getters and setters

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public double[] getOutputVector() {
        return outputVector;
    }

    public void setOutputVector(double[] outputVector) {
        this.outputVector = outputVector;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public void setHiddenSize(int hiddenSize) {
        this.hiddenSize = hiddenSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public double[][] getFirstLayerWeights() {
        return firstLayerWeights;
    }

    public void setFirstLayerWeights(double[][] firstLayerWeights) {
        this.firstLayerWeights = firstLayerWeights;
    }

    public double[] getFirstLayerBiases() {
        return firstLayerBiases;
    }

    public void setFirstLayerBiases(double[] firstLayerBiases) {
        this.firstLayerBiases = firstLayerBiases;
    }

    public double[][] getSecondLayerWeights() {
        return secondLayerWeights;
    }

    public void setSecondLayerWeights(double[][] secondLayerWeights) {
        this.secondLayerWeights = secondLayerWeights;
    }

    public double[] getSecondLayerBiases() {
        return secondLayerBiases;
    }

    public void setSecondLayerBiases(double[] secondLayerBiases) {
        this.secondLayerBiases = secondLayerBiases;
    }

    // set random parameters, for testing purposes
    public void setRandomNetworkParameters(){
        for(int i = 0; i < hiddenSize; i++){
            for(int j = 0; j < inputSize; j++) {
                firstLayerWeights[i][j] = Math.random();
            }
        }

        for(int i = 0; i < hiddenSize; i++){
            firstLayerBiases[i] = Math.random();
        }

        for(int i = 0; i < outputSize; i++){
            for(int j = 0; j < hiddenSize; j++) {
                secondLayerWeights[i][j] = Math.random();
            }
        }

        for(int i = 0; i < outputSize; i++){
            secondLayerBiases[i] = Math.random();
        }
    }


    // printers, for debug purposes
    public void printNetworkParameteres(){
        System.out.println("firstLayerWeights:");
        for(int i = 0; i < hiddenSize; i++){
            for(int j = 0; j < inputSize; j++) {
                System.out.print(firstLayerWeights[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("\nfirstLayerBiases:");
        for(int i = 0; i < hiddenSize; i++){
            System.out.print(firstLayerBiases[i] + " ");
        }
        System.out.println("");

        System.out.println("\nsecondLayerWeights:");
        for(int i = 0; i < outputSize; i++){
            for(int j = 0; j < hiddenSize; j++) {
                System.out.print(secondLayerWeights[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("\nsecondLayerBiases:");
        for(int i = 0; i < outputSize; i++){
            System.out.print(secondLayerBiases[i] + " ");
        }
        System.out.println("\n");
    }
}
