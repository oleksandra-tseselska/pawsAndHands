package com.pawsandhands.Adoption;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface AdoptionRepository extends CrudRepository<Adoption,Long> {

    ArrayList<Adoption> findAll();

    ArrayList<Adoption> findAllByReservedIsFalse();  //To find all pets who are available for reservation

    ArrayList<Adoption> findAllByReservedIsTrue();  //To find all pets who are already reserved

    ArrayList<Adoption> findAdoptionsByUser(User user); //To find all pets who are already reserved by selected user

}
