package com.guessNumbersWithAI.model;
// No training will be done here. The neural network parameters will be simply loaded from the database
// I will provide two options to the user: to load parameters of NN that I've trained myself in my other
// project, or to load parameters that have been obtained from state-of-art PyTorch training method

public class Network {

    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private double[] inputVector; // will be provided by user
    private double[] hiddenVector; // null at the beginning
    private double[] outputVector;
    // the neural network parameters that will be loaded from the database
    private double[][] firstLayerWeights;
    private double[] firstLayerBiases;
    private double[][] secondLayerWeights;
    private double[] secondLayerBiases;

    // I will let the user load the parameters from the MySQL DB
    public Network(int inputSize, int hiddenSize, int outputSize,
                   double[][] firstLayerWeights, double[] firstLayerBiases,
                   double[][] secondLayerWeights, double[] secondLayerBiases) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.inputVector = new double[inputSize];
        this.hiddenVector = new double[hiddenSize];
        this.outputVector = new double[outputSize];
        this.firstLayerWeights = firstLayerWeights;
        this.firstLayerBiases = firstLayerBiases;
        this.secondLayerWeights = secondLayerWeights;
        this.secondLayerBiases = secondLayerBiases;
    }

    // Forward propagation: the neural network gets user image input, that is converted to the input vector
    // and returns as an output the probabilities for different numbers
    public double[] forward(double[] input){
        // forwardprop is very simple really; just do the following:
        // 1. Compute [z] = [firstLayerWeights][input] + [firstLayerBiases];
        // 2. Obtain the activation values of the hidden vector [y] by applying to [z] some activation function f([z]).
        //    Here we use ReLU activation: f(z) > 0 ? z : 0;
        // 3. Repeat for the next layer with secondLayerWeights and secondLayerBiases to get the [output] vector.

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

}
