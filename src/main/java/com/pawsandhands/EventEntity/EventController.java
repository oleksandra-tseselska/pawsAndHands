package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        try {
            model.addAttribute(
                    "eventList",
                    this.eventService.findAllByDateIsAfterOrderByDate(dateNow)
            );
        }catch (Exception e){
            return "redirect:all-events" + e.getMessage();
        }

        return "all-events";
    }

    @GetMapping("/view-event/{eventId}")
    public String viewEventInfo(@PathVariable Integer eventId,
                               Model model)
    {

        try{
            model.addAttribute("eventData", eventService.findById(eventId));
        }catch (Exception e){
            return "redirect:all-events?message=search_filed&error=" + e.getMessage();
        }

        return "view-event";
    }


}
