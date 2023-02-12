package com.pawsandhands.PetEntity;
import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired //dependency injection
    public PetService(PetRepository petRepository, UserRepository userRepository){
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public void createPet(Pet pet) throws Exception{
        this.petRepository.save(pet);
    }

    public void updatePetWithOwner(Pet pet, Long ownerId) {
        try {
            Pet petFromDb = this.petRepository.findPetById(pet.getId());
            User userFromDb = userRepository.findUserById(ownerId);
            Set<User> setOfOwners = new HashSet<>();

            setOfOwners.addAll(petFromDb.getPetOwners());
            setOfOwners.add(userFromDb);

            petFromDb.setPetOwners(setOfOwners);
            this.petRepository.save(petFromDb);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public Pet findPetById(Long petId) throws Exception {
        return this.petRepository.findById(petId).orElseThrow();
    }

    public ArrayList<Pet> findAllByOrderByNickname() throws Exception {
        return this.petRepository.findAllByOrderByNickname();
    }

}
