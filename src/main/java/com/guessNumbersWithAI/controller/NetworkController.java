package com.guessNumbersWithAI.controller;

import com.guessNumbersWithAI.model.Network;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NetworkController {

    @GetMapping("/")
    public String hangleGetMapping(Model theModel){

        // the MNIST image training set consists of 28x28 pixel images, and we provide grayscale pixel
        // values as an input; hidden size choice is arbitrary,
        Network ourNetwork = new Network();
        //System.out.println("Network generated");

        theModel.addAttribute("NNmodel", ourNetwork);
        //System.out.println("Network added to model attributes");

        return "main-page";
    }

    @PostMapping("/")
    public String hanglePostMapping(@ModelAttribute("NNmodel") Network ourNetwork){

        System.out.println("where are in baby! Chosen model: " + ourNetwork.getChosenModel());
        System.out.println("where are in baby! Test field: " + ourNetwork.getTestField());
        System.out.println("Our input vector: ");
        for(int k = 0; k < ourNetwork.getRawInput().size(); k++ ){
            if (k % 280 == 0) System.out.println("");
            System.out.printf(ourNetwork.getRawInput().get(k) + " ");
        }

        return "redirect:/";
    }

}
