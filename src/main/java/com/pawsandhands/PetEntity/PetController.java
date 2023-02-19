package com.pawsandhands.PetEntity;

import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PetController {
    private final PetService petService;
    private final UserService userService;

    @Autowired
    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @GetMapping("/create-pet")
    public String showCreatePetPage(
        Model model,
        @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
        @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")){
            return "not-logged-in";
        }else {
            try {
//                System.out.println(userIdFromCookie);
//                System.out.println(userIsLoggedInFromCookie);

                model.addAttribute("user", userService.findUserById(Long.valueOf(userIdFromCookie)));

                return "create-pet";
            } catch (Exception e) {
                return "not-logged-in";
                //            throw new RuntimeException(e); not needed anymore, since the returning erorr
            }
        }
    }


    @PostMapping("/create-pet")
    public String handlePetCreation(
        Pet pet,
        Model model,
        @CookieValue(value = "userId") String userIdFromCookie ,
        @CookieValue(value="userIsLoggedIn") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        try{
//            System.out.println(pet);
//            System.out.println(userIdFromCookie);
            model.addAttribute("user", userService.findUserById(Long.valueOf(userIdFromCookie)));
            User user = userService.findUserById(Long.valueOf(userIdFromCookie));
//            System.out.println(user);
            this.petService.createPet(pet);

            this.petService.updatePetWithOwner(pet, Long.valueOf(userIdFromCookie));

            this.userService.updateOwnerWithPet(Long.valueOf(userIdFromCookie), pet);

            model.addAttribute("message", "signup_success");
            model.addAttribute("petId", pet.getId());
            model.addAttribute("petName", pet.getNickname());

            return "create-pet-success";
        }
        catch(Exception e){
            model.addAttribute("message", "pet_profile_creation_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pet", pet);
            return "redirect:create-pet?message=pet_profile_creation_failed";

        }
    }

    @GetMapping("/create-pet-success")
    public String showCreatePetSuccessPage(
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        return "create-pet-success"; //will find register in the Template folder. When we want to display the page
    }

    @GetMapping("pet-profile/{petId}")
    public String displayPetProfile(
            @PathVariable Long petId,
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ) {
        try {
            model.addAttribute("pet", petService.findPetById(petId));
            model.addAttribute("petOwners", petService.findPetById(petId).getPetOwners());

            for(User u : petService.findPetById(petId).getPetOwners()){
                if(userIdFromCookie.equals(Long.toString(u.getId()))){
                    model.addAttribute("currentUserIsPetOwner", true);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "pet-profile";
    }

    @GetMapping("pet-profile/{petId}/edit")
    public String displayPetEditProfile(
            @PathVariable Long petId,
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ) {
        try {

            model.addAttribute("pet", petService.findPetById(petId));
            model.addAttribute("petOwners", petService.findPetById(petId).getPetOwners());
            model.addAttribute("allUsers", userService.findAll());

            for(User u : petService.findPetById(petId).getPetOwners()){
                if(userIdFromCookie.equals(Long.toString(u.getId()))){
                    model.addAttribute("currentUserIsPetOwner", true);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "pet-profile-edit";
    }

    @PostMapping("/pet-profile/{petId}/edit/update")
    public String handlePetUpdate(
            @PathVariable Long petId,
            Pet pet,
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        try{
//            model.addAttribute("user", userService.findUserById(Long.valueOf(userIdFromCookie)));
            this.petService.updatePet(pet, petId);
            model.addAttribute("message", "update_success");
            model.addAttribute("petId", pet.getId());
            model.addAttribute("petName", pet.getNickname());
            model.addAttribute("petOwners", petService.findPetById(petId).getPetOwners());
            model.addAttribute("allUsers", userService.findAll());
            return "pet-profile-edit";
        }
        catch(Exception e){
            model.addAttribute("message", "pet_profile_update_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pet", pet);
            return "redirect:pet-profile-edit?message=pet_profile_update_failed";

        }
    }

    @PostMapping("/pet-profile/{petId}/edit/addOwner")
    public String handleAddOwnerUpdate(
//            Pet pet,
            Model model,
            @PathVariable String petId,
            @RequestParam(name="owner") String newOwnerId,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ) {
        try {
            Pet petToUpdate = petService.findPetById(Long.valueOf(petId));

            this.petService.updatePetWithOwner(petToUpdate, Long.valueOf(newOwnerId));
            this.userService.updateOwnerWithPet(Long.valueOf(newOwnerId), petToUpdate);
            model.addAttribute("pet", petService.findPetById(Long.valueOf(petId)));
            model.addAttribute("petOwners", petService.findPetById(Long.valueOf(petId)).getPetOwners());
            model.addAttribute("message", "success_owners_updated");
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("petId", petId);
            return "pet-profile-edit";

        } catch (Exception e) {
            model.addAttribute("message", "pet_profile_update_with_owner_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("petId", petId);
            model.addAttribute("allUsers", userService.findAll());

            return "redirect:/pet-profile/" + petId + "?message=" + e.getMessage();
        }
    }

    @GetMapping("/pet-profile/{petId}/edit/deleteOwner/{ownerId}")
    public String handleDeleteOwnerUpdate(
            Pet pet,
            Model model,
            @PathVariable String petId,
            @PathVariable String ownerId,
            @CookieValue(value = "userId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ) {
        try {
            Pet petToUpdate = petService.findPetById(Long.valueOf(petId));
//            this.petService.deleteOwnerFromPet(petToUpdate, Long.valueOf(ownerId));
            this.userService.deletePetFromUser(Long.valueOf(ownerId), petToUpdate);

            model.addAttribute("pet", petService.findPetById(Long.valueOf(petId)));
            model.addAttribute("petOwners", petService.findPetById(Long.valueOf(petId)).getPetOwners());
            model.addAttribute("message", "success_owner_deleted");
            model.addAttribute("petId", pet.getId());
            model.addAttribute("petName", pet.getNickname());
            model.addAttribute("allUsers", userService.findAll());

            return "pet-profile-edit";
        } catch (Exception e) {
            model.addAttribute("message", "pet_profile_update_with_owner_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pet", pet);
            return "redirect:pet-profile-edit?message=pet_profile_update_with_owner_failed/" + e.getMessage();

        }
    }

    @GetMapping("/pet-profile/{petId}/delete")
    public String handlePetDeletion(
            Model model,
            @PathVariable String petId,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ) {
        try {
            this.petService.deletePet(Long.valueOf(petId));
            model.addAttribute("message", "pet_was_deleted");

            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("message", "pet_deletion_failed");
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/profile?message=pet_deletion_failed/";

        }
    }






    @GetMapping("all-pets")
    public String showAllPetsPage(
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            model.addAttribute("petList", petService.findAllByOrderByNickname());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "all-pets";
    }


}
