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

    public void updatePet(Pet petNew, Long id)
//            throws Exception
    {
        try {
            Pet petOld = petRepository.findPetById(id);
            petNew.setId(petOld.getId());
            petNew.setCreatedAt(petOld.getCreatedAt());
            petNew.setPhoto(petOld.getPhoto());
            petNew.setPhotoPath(petOld.getPhotoPath());


            if(petNew.getBreed().equals("empty") && !(petOld.getBreed().equals("empty"))){
                petNew.setBreed(petOld.getBreed());
            }

            if((petOld.getCountry() != null) && (petNew.getCountry() == null)) {
                petNew.setCountry(petOld.getCountry());
            }

            if((petOld.getCountry() != null) && (petNew.getCountry() != null)) {
                if (petNew.getCountry().equals("--") && !(petOld.getCountry().equals("--"))) {
                    petNew.setCountry(petOld.getCountry());
                }
            }

            if((petNew.getPetOwners() == null) && (petOld.getPetOwners() != null)){
                petNew.setPetOwners(petOld.getPetOwners());
            }

            if((petNew.getPhoto() == null) && (petOld.getPhoto() != null)){
                petNew.setPhoto(petOld.getPhoto());
            }

            if((petNew.getPhotoPath() == null) && (petOld.getPhotoPath() != null)){
                petNew.setPhotoPath(petOld.getPhotoPath());
            }

            this.petRepository.save(petNew);
        }
        catch (Exception e){
            System.out.println(e);

        }

    }
    public Pet save(Pet pet){
        return this.petRepository.save(pet);
    }



    public void updatePetWithOwner(Pet pet, Long ownerId) {
        try {
            Pet petFromDb = this.petRepository.findPetById(pet.getId());
            User userFromDb = userRepository.findUserById(ownerId);
            Set<User> setOfOwners = new HashSet<>();

            if(!(petFromDb.getPetOwners() == null)){
                setOfOwners.addAll(petFromDb.getPetOwners());
            }

            setOfOwners.add(userFromDb);

            petFromDb.setPetOwners(setOfOwners);
            this.petRepository.save(petFromDb);
        } catch (Exception e){
            System.out.println("updatePetWithOwner" + e);
        }
    }

    public Pet findPetById(Long petId) throws Exception {
        return this.petRepository.findById(petId).orElseThrow();
    }

    public ArrayList<Pet> findAllByOrderByNickname() throws Exception {
        return this.petRepository.findAllByOrderByNickname();
    }

    public void deletePet(Long petToDeleteId) {
        Pet petToDelete = this.petRepository.findPetById(petToDeleteId);
//        userRepository.deleteAllUsersFromPet(petToDelete);
//        Set<User> setOfOwners = new HashSet<>();
//        Set<User> setOfPets = new HashSet<>();
//
//        petToDelete.setPetOwners(setOfOwners);
//        this.petRepository.save(petToDelete);
//        System.out.println(petToDelete);
        this.petRepository.delete(petToDelete);
        System.out.println("pet was deleted");


    }

    public void deleteOwnerFromPet(Pet petToUpdate, Long userIoDelete) {
            try {
                Pet petFromDb = petRepository.findPetById(petToUpdate.getId());
                User userFromDb = userRepository.findUserById(userIoDelete);
                Set<User> setOfUsers = new HashSet<>();
                setOfUsers.addAll(petFromDb.getPetOwners());
                setOfUsers.remove(userFromDb);
                petFromDb.setPetOwners(setOfUsers);
                System.out.println("deleteOwnerFromPet0" + setOfUsers);
                this.userRepository.save(userFromDb);
                System.out.println("deleteOwnerFromPet1" + userRepository.findUserById(userIoDelete));

            } catch (Exception e){
                System.out.println("deleteOwnerFromPet2" + e);
            }

    }
}
