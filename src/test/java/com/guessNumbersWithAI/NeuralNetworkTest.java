package com.guessNumbersWithAI;

import com.guessNumbersWithAI.model.NeuralNetwork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class NeuralNetworkTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    NeuralNetwork neuralNetwork;

    @BeforeEach
    public void setupNeuralNetwork(){
        neuralNetwork = new NeuralNetwork(); // reset all neural network parameters to zero
    }

    @DisplayName("Test forward() method")
    @Test
    public void testForward(){

        // generate random input vector
        double[] input = new double[28*28];
        for(int i = 0; i < input.length; i++){
            input[i] = Math.random();
        }

        // passing input vector to NeuralNetwork with all parameters set to null should return null output vector
        neuralNetwork.forward(input);
        double[] output = neuralNetwork.getOutputVector();
        double sum = DoubleStream.of(output).sum();

        assertEquals(0.0, sum, "passing input to empty network should return empty vector");


        // set random parameters and check that forward() produces non-zero output vector
        neuralNetwork.setRandomNetworkParameters();
        neuralNetwork.forward(input);
        output = neuralNetwork.getOutputVector();
        assertNotEquals(0.0,  DoubleStream.of(output).sum(),
                "the output vector should not contain only zeros");


        // output vector should not contain negative values
        boolean contains = DoubleStream.of(output).anyMatch(x -> x < 0);
        assertFalse(contains, "the output vector should not contain any negative elements");


        // the answer field should be equal to the biggest element value from output vector
        double networkAnswer = output[neuralNetwork.getAnswer()]; // getAnswer() returns array's id
        Arrays.sort(output);
        double answer = output[output.length-1];

        assertEquals(answer, networkAnswer,
                "The answer that forward() produces must be the element of output with max value");
    }


    @DisplayName("Test loading of trained neural network parameters")
    @Test
    public void testLoadNetworkParameters() throws Exception{

        // provide non-existing file and test for I/O FileNotFoundException
        Resource resource1 = resourceLoader.getResource(
                "classpath:net_params.txt");
        assertThrows(FileNotFoundException.class, () -> {InputStream inputStream = resource1.getInputStream();},
                "trying to load non-existing file should throw I/O FileNotFoundException" );

        // load trained neural network parameters
        Resource resource2 = resourceLoader.getResource(
                "classpath:net_params_size784_256_10_lr0.001_trainEps100.txt");
        InputStream inputStream = resource2.getInputStream();
        neuralNetwork.loadNetworkParameters(inputStream);

        // check that arrays are not empty now
        double[] firstLayerBiases = neuralNetwork.getFirstLayerBiases();
        boolean firstLayerBiasesHasNonzeroElem = DoubleStream.of(firstLayerBiases).anyMatch(x -> x!=0);
        assertTrue(firstLayerBiasesHasNonzeroElem,
                "all elements cannot be zero in double[] firstLayerBiases");

        double[][] firstLayerWeights = neuralNetwork.getFirstLayerWeights();
        DoubleStream weightsStream1 = Arrays.stream(firstLayerWeights).flatMapToDouble(x -> Arrays.stream(x));
        boolean firstLayerWeightsHasNonzeroElem = weightsStream1.anyMatch(x -> x!=0);
        assertTrue(firstLayerWeightsHasNonzeroElem,
                "all elements cannot be zero in double[][] firstLayerWeights");

        double[] secondLayerBiases = neuralNetwork.getSecondLayerBiases();
        boolean secondLayerBiasesHasNonzeroElem = DoubleStream.of(secondLayerBiases).anyMatch(x -> x!=0);
        assertTrue(secondLayerBiasesHasNonzeroElem,
                "all elements cannot be zero in double[] secondLayerBiases");

        double[][] secondLayerWeights = neuralNetwork.getSecondLayerWeights();
        DoubleStream weightsStream2 = Arrays.stream(secondLayerWeights).flatMapToDouble(x -> Arrays.stream(x));
        boolean secondLayerWeightsHasNonzeroElem = weightsStream2.anyMatch(x -> x!=0);
        assertTrue(secondLayerWeightsHasNonzeroElem,
                "all elements cannot be zero in double[][] secondLayerWeights");
    }

}
