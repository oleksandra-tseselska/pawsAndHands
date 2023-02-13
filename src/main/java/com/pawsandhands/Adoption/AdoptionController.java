package com.pawsandhands.Adoption;

import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdoptionController {

    private AdoptionService adoptionService;
    private UserService userService;

    public AdoptionController(AdoptionService adoptionService, UserService userService) {
        this.adoptionService = adoptionService;
        this.userService = userService;
    }


    @GetMapping("/adoption")
    public String showAllPetsForAdoption(
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            model.addAttribute("adoptionPetList", adoptionService.findAllPetsForAdoption());

        } catch (Exception e) {
            return "redirect:all-pets-adoption" + e.getMessage();  // this direction can be changed
        }
        return "all-pets-adoption";
    }

    @GetMapping("/reserve")
    public String reservePetPage(){
        return "reservation-approval";
    }

    @PostMapping("/reserve")
    public String reservePet(
           // @PathVariable Long petId,
            Model model,
            @RequestParam(name = "petId", required = false) Long petId,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println(userIdFromCookie);
            User userWhoReservesPet = userService.findUserById(Long.valueOf(userIdFromCookie));
            Adoption reservedPet = adoptionService.findAdoptedPetById(petId);

            reservedPet.setUser(userWhoReservesPet);
            reservedPet.setReserved(true);
            adoptionService.createPetForAdoption(reservedPet);


        }catch (Exception e){
            return "redirect:/adoption" + e.getMessage();          //Endpoint can be changed !!!
        }

        return "reservation-approval";
    }


}
