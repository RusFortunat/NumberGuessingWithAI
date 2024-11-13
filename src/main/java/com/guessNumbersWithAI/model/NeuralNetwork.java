// I will not be training the neural networks here, but will simply upload their parameters from file.
// The first trained neural network model was obtained by me writing the whole supervised learning
// machinery in Java from scratch, and the second was obtained with use of PyTorch machine learning package.
// If you are interested in how I trained these two neural networks, check out this project:
// My implementation from scratch in Java: https://github.com/RusFortunat/java_ML_library
// Python PyTorch and my C++ implementation: https://github.com/RusFortunat/alternative-ML-lib-C2plus

package com.guessNumbersWithAI.model;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class NeuralNetwork {

    // I have two trained networks, and this parameter determines which of them the user will use
    public String chosenNetworkModel;

    private int inputSize;
    private int hiddenSize;
    private int outputSize;

    // the neural network parameters that will be loaded from the file
    private double[][] firstLayerWeights;
    private double[] firstLayerBiases;
    private double[][] secondLayerWeights;
    private double[] secondLayerBiases;

    // i explicitly define it here so that it can be displayed on the view page
    private int answer;


    // I will let the user to choose which neural network to use and load parameters later
    public NeuralNetwork() {

        this.chosenNetworkModel = "";

        this.inputSize = 28*28; // MNIST training images are of 28x28 pixel size
        this.hiddenSize = 256; // arbitrary, should not be too small or too big
        this.outputSize = 10; // 0-9 digits that network will be guessing
        this.firstLayerWeights = new double[hiddenSize][inputSize];
        this.firstLayerBiases = new double[hiddenSize];
        this.secondLayerWeights = new double[outputSize][hiddenSize];
        this.secondLayerBiases = new double[outputSize];
    }


    // Forward propagation: the neural network gets user image input, that is converted to the input vector
    // and returns as an output the probabilities for different numbers
    public void forward(double[] input){
        // forward propagation is not that hard, just do the following:
        // 1. Take matrix product and add biases [z] = [firstLayerWeights]*[input] + [firstLayerBiases];
        // 2. Obtain hidden activation values [y] by applying to [z] some activation function f([z]), i.e., mapping.
        //    Here we use ReLU activation / mapping: f(z) > 0 ? z : 0;
        // 3. Repeat 1.-2. for the next layer with secondLayerWeights and secondLayerBiases to get the [output] vector.

        double[] hiddenVector = new double[hiddenSize];
        double[] outputVector = new double[outputSize];

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
                //sum+= activation; // no relu on output values
            }
            outputVector[i] = sum;
            totalSum += Math.exp(sum);
        }

        // normalize the output vector using the SoftMax approach;
        // i will keep it for future, if i'll need the whole vector
        for(int i = 0; i < outputSize; i++){
            outputVector[i] = Math.exp(outputVector[i]) / totalSum;
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

        answer = maxId;
    }


    // loads the parameters of the chosen network
    public void loadNetworkParameters(){

        try{

            // specify filename for the model that user selected
            String filename = "src/main/resources/net_params_size784_256_10_lr0.001_trainEps100.txt";
            // working on it
            /*if(chosenNetworkModel.equals("PyTorch")){
                filename = "src/main/resources/PyTorch_params.txt";
            }else if(chosenNetworkModel.equals("MyModel")){
                filename = "src/main/resources/net_params_size784_256_10_lr0.001_trainEps100.txt";
            }*/

            // load the file
            File networkParamsFile = new File(filename);
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

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // getters and setters

    public String getChosenNetworkModel() {
        return chosenNetworkModel;
    }

    public void setChosenNetworkModel(String chosenNetworkModel) {
        this.chosenNetworkModel = chosenNetworkModel;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
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
