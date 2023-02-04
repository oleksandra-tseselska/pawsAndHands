package com.pawsandhands.EventEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

    @GetMapping("/create-event")
    public String showCreateEventPage(){
        return "create-event";
    }

}
