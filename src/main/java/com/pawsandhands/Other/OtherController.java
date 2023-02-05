package com.pawsandhands.Other;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {

    @GetMapping("/about-us")
    public String showAboutUsPage(){
        return "about-us"; //will find register in the Template folder. When we want to display the page
    }
}
