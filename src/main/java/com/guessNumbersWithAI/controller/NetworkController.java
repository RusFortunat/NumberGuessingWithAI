package com.guessNumbersWithAI.controller;

import com.guessNumbersWithAI.model.Network;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NetworkController {

    private Network ourNetwork;

    @RequestMapping("/")
    public String ourMainView(Model theModel){

        theModel.addAttribute("NNmodel", ourNetwork);

        return "main-page";
    }
}
