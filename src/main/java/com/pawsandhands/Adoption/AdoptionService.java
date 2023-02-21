package com.pawsandhands.Adoption;

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

    public void createPetForAdoption(Adoption petForAdoption){
        this.adoptionRepository.save(petForAdoption);
    }

    public void deletePetForAdoption(Long petId){
        this.adoptionRepository.deleteById(petId);
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

    public Adoption findAdoptionPetById(Long adoptionPetId){
        return adoptionRepository.findAdoptionsById(adoptionPetId);

    }

}
