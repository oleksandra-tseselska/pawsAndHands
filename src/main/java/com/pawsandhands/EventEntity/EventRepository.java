package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EventRepository extends CrudRepository<Event,Long> {

    Event findByName(String eventName);

    ArrayList<Event> findAll();
}
