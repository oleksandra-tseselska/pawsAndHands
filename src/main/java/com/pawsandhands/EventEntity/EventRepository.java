package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Modifying
    @Transactional
    @Query(value = "delete from Event e where e.id = ?1")
    void deleteById(Long id);

    Long deleteEventById(Long id);

    ArrayList<Event> findEventByUser(User user);        //For My Events section

    ArrayList<Event> findAll();
    ArrayList<Event> findAllByOrderByDate();

//    ArrayList<Event> findFirst3ByDateIsAfterOrderByDate(String date);
}
