package com.pawsandhands.PetEntity;

import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
                System.out.println(userIdFromCookie);
                System.out.println(userIsLoggedInFromCookie);

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
            System.out.println(pet);
            System.out.println(userIdFromCookie);
            model.addAttribute("user", userService.findUserById(Long.valueOf(userIdFromCookie)));
            User user = userService.findUserById(Long.valueOf(userIdFromCookie));
            System.out.println(user);
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
    public String showCreatePetSuccessPage(){
        return "create-pet-success"; //will find register in the Template folder. When we want to display the page
    }

    @GetMapping("pet-profile/{petId}")
    public String displayPetProfile(
            @PathVariable Long petId,
            Model model
    ) {
        try {
            model.addAttribute("pet", petService.findPetById(petId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "pet-profile";
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
