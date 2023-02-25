package com.pawsandhands.EventEntity;

import com.pawsandhands.FileStorage.FilesStorageServiceImpl;
import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    private EventService eventService;
    private UserService userService;
    private final FilesStorageServiceImpl filesStorage;

    private final Path imgEventsPath = Paths.get("src/main/resources/static/img/event-photo");

    @Autowired
    public EventController(EventService eventService, UserService userService, FilesStorageServiceImpl filesStorage) {
        this.eventService = eventService;
        this.userService = userService;
        this.filesStorage = filesStorage;
    }

    @GetMapping("/discounts")
    public String showDiscounts(
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        return "discounts";
    }


    @GetMapping("/create-event")
    public String showCreateEventPage(
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        return "create-event";
    }


    @PostMapping("/createEvent")
    public String handleEventCreation(Event event,
          @CookieValue(value = "userId") String userIdFromCookie
    ) {
        try {
            System.out.println(userIdFromCookie);
            User eventUser = userService.findUserById(Long.valueOf(userIdFromCookie));
            event.setUser(eventUser);
            this.eventService.createEvent(event);
        } catch (Exception e) {
            return "redirect:/my-events/" + e.getMessage();
        }
        return ("redirect:/my-events");
    }


    @GetMapping("/all-events")
    public String showAllEvents(Model model,  @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie){
        ArrayList<Event> eventsAfterNow = findEventsAfterNow();

        try {
            //     get photos of all events from DB
            for(Event event: eventsAfterNow){
                if(event.getPhoto() != null){
                    String pathFileUser = "event_photo_" +event.getId()+ ".png";
                    this.filesStorage.base64DecodedString(event.getPhoto(), pathFileUser, imgEventsPath);
                }
            }

            model.addAttribute(
//                    "eventList", findEventsAfterNow());          //Wouldn't it be better to see ALL EVENTS?
                    "eventList", findTop3(eventsAfterNow));

            User user = userService.findUserById(Long.valueOf(userIdFromCookie)); //fining User in our DB

            //Checking if User is Admin, if yes => in html additional button will appear => to delete event
            if (user.isAdmin()) {
                model.addAttribute("currentUserIsAdmin", true);
            }

        }catch (Exception e){
            return "redirect:/all-events/" + e.getMessage();
        }

        return "/all-events";
    }


    @GetMapping("/my-events")
    public String showMyEvents(Model model,
           @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
           @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        try {
            System.out.println(userIdFromCookie);
            User userWhoCreatedEvents = userService.findUserById(Long.valueOf(userIdFromCookie));

            ArrayList<Event> myEvents = this.eventService.findEventsByUser(userWhoCreatedEvents);

            //     get photos of all events from DB
            for(Event event: myEvents){
                if(event.getPhoto() != null){
                    String pathFileUser = "event_photo_" +event.getId()+ ".png";
                    this.filesStorage.base64DecodedString(event.getPhoto(), pathFileUser, imgEventsPath);
                }
            }

            model.addAttribute("myEventsList", myEvents);
            return "my-events";

        }catch (Exception e){
            return "redirect:my-events/?message=" + e.getMessage();
        }
    }

    @PostMapping("/delete-event")
    public String deleteEvent(
            @RequestParam(name = "EventId", required = false) Long eventId,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println("Event with following id was deleted: " + eventId);
            this.eventService.deleteEvent(eventId);

        }catch (Exception e){
            return "redirect:/my-events/" + e.getMessage();
        }
        return "redirect:/my-events";
    }


    @GetMapping("/update-event")                                        //to get filled in form
    public String updateEventView(
            Model model,
            @RequestParam(name = "EventId", required = false) int eventId,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            model.addAttribute("event",eventService.findById(eventId));
            System.out.println("Event id to be updated: " + eventId);

        }catch (Exception e){
            return "redirect:all-events?message=search_filed&error=" + e.getMessage();          //Endpoint can be changed !!!
        }
        return "edit-event";}


    @PostMapping("/update-event")
    public String handleEventUpdate(Event event,
                                    @RequestParam(name = "EventId", required = false) int eventId,
                                    @CookieValue(value = "userId") String userIdFromCookie
    ){
        try {
            Event oldEventData = eventService.findById(eventId);

            //Setting creation date info (as it is not done manually by user)
            event.setCreatedAt(oldEventData.getCreatedAt());

            //Finding and setting user for event (as it is not done manually by user)
            User eventUser = userService.findUserById(Long.valueOf(userIdFromCookie));
            event.setUser(eventUser);

            //Setting again this event id (as it is not done manually by user)
            event.setId(eventId);
            this.eventService.createEvent(event);

        } catch (Exception e) {
            return "redirect:/my-events/" + e.getMessage();
        }
        return ("redirect:/my-events");
    }

    //   Add Photo Start
    @GetMapping ("/edit-event-photo/{eventId}")
    public String showPetPhoto(Model model,
                               @PathVariable Long eventId
    ){
        try {
//            delete temp img
            this.filesStorage.deleteAll();

//            data from DB
            Integer eventIdInt = eventId != null ? eventId.intValue() : null;
            model.addAttribute("event", eventService.findById(eventIdInt));

            return "edit-event-photo";

        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    @PostMapping("/add-photo/eventId/{eventId}")
    public String editEventPhoto(@PathVariable Long eventId,
                               @RequestParam("photo") MultipartFile multipartFile){
        try{
//            add folder for temp photos
            this.filesStorage.init();

            Integer eventIdInt = eventId != null ? eventId.intValue() : null;
            Event event = this.eventService.findById(eventIdInt);
            System.out.println(event.toString());
            String pathFileEvent;
            String pathEventPhoto;
            Path eventPhotoPath;

            pathFileEvent = "event_photo_" +eventId.toString()+ ".png";
            pathEventPhoto = "/img/event-photo/event_photo_"+eventId+ ".png";


//            file to Base64 and save
            eventPhotoPath = this.imgEventsPath.resolve(Paths.get(pathFileEvent));
            System.out.println(eventPhotoPath +" eventPhotoPath absolut");
            String encodedString = this.filesStorage.base64EncodedString(multipartFile);
//            System.out.println(encodedString);
            this.filesStorage.save(multipartFile, eventPhotoPath, pathFileEvent);

//            send data to DB
            event.setPhoto(encodedString);
            event.setPhotoPath(pathEventPhoto);
            this.eventService.createEvent(event);

        }catch (Exception e){
            e.getMessage();
        }

        return "redirect:/spinner-event/"+eventId.toString();
    }

    @GetMapping ("/spinner-event/{eventId}")
    public String showSpinnerPet(@PathVariable Integer eventId,
                                 Model model){
        try {
            model.addAttribute("event", eventService.findById(eventId));

            return "spinner-event";
        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    //   Add Photo End


    @GetMapping("/view-event")   //("/view-event/{eventId}")
    public String viewEventInfo(
//            @PathVariable Integer eventId,
            @RequestParam(name = "EventId", required = false) int eventId,
                                Model model)
    {
        try{
//            delete temp img
            this.filesStorage.deleteAll();

//            get photo from DB
            Event event = this.eventService.findById(eventId);

            if(event.getPhoto() != null){
                String pathFileUser = "event_photo_" +event.getId()+ ".png";
                this.filesStorage.base64DecodedString(event.getPhoto(), pathFileUser, imgEventsPath);
            }

            model.addAttribute("eventData", eventService.findById(eventId));
        }catch (Exception e){
            return "redirect:all-events?message=search_filed&error=" + e.getMessage();
        }

        return "view-event";
    }


    private ArrayList<Event> findEventsAfterNow(){
        ArrayList<Event> eventsAfterNow = new ArrayList<>();

        for(Event event: this.eventService.findAllByOrderByDate()) {
            Date sqlEventDate = java.sql.Date.valueOf(event.getDate());

            if (sqlEventDate.toLocalDate().isAfter(LocalDate.now())) {
                eventsAfterNow.add(event);
            }
        }

        return eventsAfterNow;
    }

    private List<Event> findTop3(ArrayList<Event> events){
        List<Event> top3Events;

        if(events.size() > 3){
            top3Events = events.subList(0,3);
        } else {
            return events;
        }

        return top3Events;
    }

}