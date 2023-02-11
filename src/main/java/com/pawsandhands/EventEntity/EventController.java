package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/create-event")
    public String showCreateEventPage(){
        return "create-event";
    }


    //To change return location
    @GetMapping("/createEvent")
    public String showEventApproval(){
        return "create-event";
    }


    //To change return location
    @PostMapping("/createEvent")
    public String handleEventCreation(Event event, @CookieValue(value = "userId") String userIdFromCookie) throws Exception {
        try {
            System.out.println(userIdFromCookie);
            this.eventService.createEvent(event);
        }catch (Exception e){
            return "redirect:create-event" + e.getMessage();
        }
        return ("create-event");

    }


}
