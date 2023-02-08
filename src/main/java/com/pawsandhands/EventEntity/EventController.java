package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String handleEventCreation(Event event, User user) throws Exception {
        try {
            this.eventService.createEvent(event, user);
        }catch (Exception e){
            return "redirect:create-event" + e.getMessage();
        }
        return ("create-event");

    }

    @GetMapping("/all-events")
    public String showAllEvents(Model model){
        try {
            model.addAttribute("eventList", this.eventService.findAll());
        }catch (Exception e){
            return "redirect:all-events" + e.getMessage();
        }

        return "all-events";
    }


}
