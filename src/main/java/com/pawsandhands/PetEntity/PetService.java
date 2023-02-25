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
            Pet petOld = findPetById(id);
            petNew.setId(petOld.getId());
//            System.out.println(petNew);
//            System.out.println(petOld);

            petNew.setCreatedAt(petOld.getCreatedAt());

            if(petNew.getBreed().equals("empty") && !(petOld.getBreed().equals("empty"))){
                petNew.setBreed(petOld.getBreed());
            }
//            System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());

//            if((petOld.getCountry() == null) && (petNew.getCountry() == null)) {
//            }
//
//            if((petOld.getCountry() == null) && (petNew.getCountry() != null)) {
//            }

            if((petOld.getCountry() != null) && (petNew.getCountry() == null)) {
                petNew.setCountry(petOld.getCountry());
            }

            if((petOld.getCountry() != null) && (petNew.getCountry() != null)) {
                if (petNew.getCountry().equals("--") && !(petOld.getCountry().equals("--"))) {
                    petNew.setCountry(petOld.getCountry());
                }
            }

//            System.out.println("old getCountry: " + petOld.getCountry() + " new breed NOW: " + petNew.getCountry());


            if((petNew.getPetOwners() == null) && (petOld.getPetOwners() != null)){
                petNew.setPetOwners(petOld.getPetOwners());
            }
//            System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());


            if((petNew.getPhoto() == null) && (petOld.getPhoto() != null)){
                petNew.setPhoto(petOld.getPhoto());
            }
//            System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());


            if((petNew.getPhotoPath() == null) && (petOld.getPhotoPath() != null)){
                petNew.setPhotoPath(petOld.getPhotoPath());
            }
//            System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());


            this.petRepository.save(petNew);
//            System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());
        }
        catch (Exception e){
            System.out.println(e);

        }
//        Pet petOld = findPetById(id);
//        petNew.setId(petOld.getId());
//        System.out.println(petNew);
//        System.out.println(petOld);
//
//        petNew.setCreatedAt(petOld.getCreatedAt());
//        if(petNew.getBreed().equals("empty") && !(petOld.getBreed().equals("empty"))){
//            petNew.setBreed(petOld.getBreed());
//        }
//        System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());
//
//        if(petNew.getCountry().equals("--") && !(petOld.getCountry().equals("--"))){
//            petNew.setCountry(petOld.getCountry());
//        }
//        System.out.println("old getCountry: " + petOld.getCountry() + " new breed NOW: " + petNew.getCountry());
//
//
//        if((petNew.getPetOwners() == null) && (petOld.getPetOwners() != null)){
//            petNew.setPetOwners(petOld.getPetOwners());
//        }
//        System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());
//
//
//        if((petNew.getPhoto() == null) && (petOld.getPhoto() != null)){
//            petNew.setPhoto(petOld.getPhoto());
//        }
//        System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());
//
//
//        if((petNew.getPhotoPath() == null) && (petOld.getPhotoPath() != null)){
//            petNew.setPhotoPath(petOld.getPhotoPath());
//        }
//        System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());
//
//
//        this.petRepository.save(petNew);
//        System.out.println("old breed: " + petOld.getBreed() + "new breed NOW: " + petNew.getBreed());

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
            System.out.println(e);
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
}
