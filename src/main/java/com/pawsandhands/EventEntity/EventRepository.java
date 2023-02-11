package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Repository
public interface EventRepository extends CrudRepository<Event,Long> {

    Event findByName(String eventName);

    Event findById(Integer eventId);

    ArrayList<Event> findEventByUser(User user);        //For My Events section

    ArrayList<Event> findAll();
    ArrayList<Event> findAllByOrderByDate();

//    ArrayList<Event> findFirst3ByDateIsAfterOrderByDate(String date);
}
