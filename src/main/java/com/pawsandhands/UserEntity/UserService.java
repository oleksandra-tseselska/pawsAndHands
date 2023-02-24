package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
import com.pawsandhands.PetEntity.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
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
        Optional<User> optionalUser = this.userRepository.findById(userId);
        User user = null;

        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }else {
            throw new RuntimeException("user not found for id : " +userId);
        }

        return user;
    }

    public User save(User user){
        return this.userRepository.save(user);
    }

    public void setUserInfoById(byte[] photo, String photoPath, Long userId){
        this.userRepository.setUserInfoById(photo, photoPath, userId);
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

            if(userFromDb.getOwnedPets() != null){
                setOfPets.addAll(userFromDb.getOwnedPets());
            }

            setOfPets.add(petFromDb);


            userFromDb.setOwnedPets(setOfPets);
            this.userRepository.save(userFromDb);
            System.out.println(userRepository.findUserById(userId));

        } catch (Exception e){
            System.out.println(e);
        }
    }


    public void deletePetFromUser(Long userId, Pet petToDelete) {
        try {
            Pet petFromDb = petRepository.findPetById(petToDelete.getId());
            User userFromDb = userRepository.findUserById(userId);
            Set<Pet> setOfPets = new HashSet<>();
            setOfPets.addAll(userFromDb.getOwnedPets());
            setOfPets.remove(petFromDb);

            userFromDb.setOwnedPets(setOfPets);
            this.userRepository.save(userFromDb);
            System.out.println(userRepository.findUserById(userId));

        } catch (Exception e){
            System.out.println(e);
        }
    }

}
