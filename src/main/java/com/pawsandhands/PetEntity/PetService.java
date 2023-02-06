package com.pawsandhands.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired //dependency injection
    public PetService(PetRepository petRepository){
        this.petRepository = petRepository;
    }

    public void createPet(Pet pet) throws Exception{
        this.petRepository.save(pet);
    }

//    public Pet verifiedUser(User userLoginRequest) throws Exception{
//        User foundUser = this.userRepository.findByUsernameAndPasswordOrderByCreatedAt(userLoginRequest.getUsername(), userLoginRequest.getPassword());
//
//        if(foundUser == null){throw new Exception("usernane or password is incorrect");
//
//        }
//
//        return foundUser; // foundUser.getId() sometimes returning the ID of hte user only is needed
//    }

    public Pet findPetById(Long petId) throws Exception {
        return this.petRepository.findById(petId).orElseThrow();
    }

}
