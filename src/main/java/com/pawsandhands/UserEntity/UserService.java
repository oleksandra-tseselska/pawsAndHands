package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
import com.pawsandhands.PetEntity.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Autowired                                                   //dependency injection, constructor
    public UserService(UserRepository userRepository, PetRepository petRepository){
        this.userRepository=userRepository;
        this.petRepository = petRepository;
    }

    public void createUser(User user){
        this.userRepository.save(user);
    }

    public User verifyUser (User userLoginRequest) throws Exception {

        //Checking for a user in DB (email + password)
        User foundUser = this.userRepository.findUserByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());

        //If we don't find => we throw exception
        if(foundUser == null){
            throw new Exception("Username or password is incorrect");
        }

        //If we find it=> we return user info
        return foundUser;
    }


    public User findUserById(Long userId) throws Exception {
      return this.userRepository.findById(userId).orElseThrow();
    }



    public ArrayList<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return this.userRepository.findById(id);
    }

    public void updateOwnerWithPet(Long userId, Pet pet) {
        try {
            Pet petFromDb = petRepository.findPetById(pet.getId());
            User userFromDb = userRepository.findUserById(userId);
            Set<Pet> setOfPets = new HashSet<>();
            setOfPets.addAll(userFromDb.getOwnedPets());
            setOfPets.add(petFromDb);

            userFromDb.setOwnedPets(setOfPets);
            this.userRepository.save(userFromDb);
            System.out.println(userRepository.findUserById(userId));

        } catch (Exception e){
            System.out.println(e);
        }
    }



}
