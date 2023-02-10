package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(Event event, User user){
        event.setCreatedBy(user.getId());                     //TO CHECK WHEN USER WILL BE LOGGED IN
        this.eventRepository.save(event);
    }

    public ArrayList<Event> findAll(){
        return eventRepository.findAll();
    }

    public ArrayList<Event> findAllByOrderByDate() {
        return eventRepository.findAllByOrderByDate();
    }

    public Event findById(Integer eventId){ return eventRepository.findById(eventId);}
}
