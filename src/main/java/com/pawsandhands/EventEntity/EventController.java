package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    private EventService eventService;
    private UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/create-event")
    public String showCreateEventPage() {
        return "create-event";
    }


    //To change return location
    @GetMapping("/createEvent")
    public String showEventApproval() {
        return "create-event";
    }


    //To change return location
    @PostMapping("/createEvent")
    public String handleEventCreation(Event event, User user, @CookieValue(value = "userId") String userIdFromCookie) throws Exception {
        try {
            System.out.println(userIdFromCookie);
            User eventUser = userService.findUserById(Long.valueOf(userIdFromCookie));
            event.setUser(eventUser);
            this.eventService.createEvent(event);
        } catch (Exception e) {
            return "redirect:create-event" + e.getMessage();
        }
        return ("create-event");
    }


}