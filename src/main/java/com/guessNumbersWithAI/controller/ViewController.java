package com.guessNumbersWithAI.controller;

import com.guessNumbersWithAI.model.DrawnImages;
import com.guessNumbersWithAI.model.NeuralNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@Controller
public class ViewController {

    @Autowired
    ResourceLoader resourceLoader;

    Logger logger = LoggerFactory.getLogger(ViewController.class);

    @GetMapping("/write-number")
    public String hangleGetMapping(Model theModel){

        NeuralNetwork ourNeuralNetwork = new NeuralNetwork();
        theModel.addAttribute("NeuralNetwork", ourNeuralNetwork);

        return "main-view";
    }

    @PostMapping(value = "/getAnswerFromServer")
    public String hanglePostMapping(@ModelAttribute("NeuralNetwork") NeuralNetwork ourNeuralNetwork,
                                    @RequestBody String rawImageData){

        try{

            // create input vector by processing raw pixel image data; we turn 280x280 px image into 28x28 px image
            DrawnImages drawnImages = new DrawnImages(rawImageData);
            double[] inputVector = drawnImages.processRawInput();

            // load network parameters from the file
            Resource resource= resourceLoader.getResource(
                    "classpath:net_params_size784_256_10_lr0.001_trainEps100.txt");
            InputStream inputStream = resource.getInputStream();
            ourNeuralNetwork.loadNetworkParameters(inputStream);

            // pass input vector to network and get the prediction; the answer will be displayed on the html view page
            ourNeuralNetwork.forward(inputVector);
            System.out.println("Answer: " + ourNeuralNetwork.getAnswer());

            // save image to the H2 database for future training of our neural networks
            drawnImages.saveImageToDB();

        }catch(Exception e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }

        return "fragment/ajaxPart :: returnedAnswer";
    }
}
