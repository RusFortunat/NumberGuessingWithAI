package com.guessNumbersWithAI.controller;

import com.guessNumbersWithAI.model.DrawnImage;
import com.guessNumbersWithAI.model.NeuralNetwork;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String hangleGetMapping(Model theModel){

        NeuralNetwork ourNeuralNetwork = new NeuralNetwork();
        DrawnImage userImage = new DrawnImage();

        theModel.addAttribute("NeuralNetwork", ourNeuralNetwork);
        theModel.addAttribute("DrawnImage", userImage);

        return "main-view";
    }

    @PostMapping("/")
    public String hanglePostMapping(@ModelAttribute("NeuralNetwork") NeuralNetwork ourNeuralNetwork,
                                    @ModelAttribute("DrawnImage") DrawnImage userImage){

        // load network parameters from the file
        if(!ourNeuralNetwork.getChosenNetworkModel().isEmpty()){
            ourNeuralNetwork.loadNetworkParameters();
        }else{
            System.out.println("Wrong model filename!");
        }

        // create input vector by processing raw pixel image data; we turn 280x280 px image into 28x28 px image
        double[] inputVector = userImage.processRawInput();

        // pass input vector to network and get the prediction; the answer will be displayed on the html view page
        ourNeuralNetwork.forward(inputVector);
        System.out.println("Answer: " + ourNeuralNetwork.getAnswer());

        return "main-view";
    }
}
