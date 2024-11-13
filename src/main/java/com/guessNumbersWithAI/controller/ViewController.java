package com.guessNumbersWithAI.controller;

import com.guessNumbersWithAI.model.DrawnImages;
import com.guessNumbersWithAI.model.NeuralNetwork;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class ViewController {

    @GetMapping("/write-number")
    public String hangleGetMapping(Model theModel){

        NeuralNetwork ourNeuralNetwork = new NeuralNetwork();
        DrawnImages drawnImages = new DrawnImages();

        theModel.addAttribute("NeuralNetwork", ourNeuralNetwork);
        theModel.addAttribute("DrawnImages", drawnImages);

        return "main-view";
    }

    @PostMapping("/write-number")
    public String hanglePostMapping(@ModelAttribute("NeuralNetwork") NeuralNetwork ourNeuralNetwork,
                                    @ModelAttribute("DrawnImages") DrawnImages drawnImages){

        // load network parameters from the file
        ourNeuralNetwork.loadNetworkParameters();

        // create input vector by processing raw pixel image data; we turn 280x280 px image into 28x28 px image
        double[] inputVector = drawnImages.processRawInput();

        // pass input vector to network and get the prediction; the answer will be displayed on the html view page
        ourNeuralNetwork.forward(inputVector);
        System.out.println("Answer: " + ourNeuralNetwork.getAnswer());

        // save image to the H2 database for future neural networks training
        try{
            drawnImages.saveImageToDB();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        // list all saved images; uncomment to view DB content in sdout
        /*try{
            System.out.println("Our Image database:");
            ArrayList<String> storedImages = drawnImages.readAllImages();
            for(String image: storedImages) System.out.println(image);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }*/

        return "main-view";
    }
}
