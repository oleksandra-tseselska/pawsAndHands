package com.pawsandhands.Adoption;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdoptionService {

    private AdoptionRepository adoptionRepository;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    public ArrayList<Adoption> findAllPetsForAdoption(){
        return adoptionRepository.findAll();
    }

    public ArrayList<Adoption> findAvailablePetsForAdoption(){
        return adoptionRepository.findAllByReservedIsFalse();
    }

    public ArrayList<Adoption> findReservedPetsForAdoption(){
        return adoptionRepository.findAllByReservedIsTrue();
    }

    public ArrayList<Adoption> findReservedPetsByUser(User user){
        return adoptionRepository.findAdoptionsByUser(user);
    }


}
