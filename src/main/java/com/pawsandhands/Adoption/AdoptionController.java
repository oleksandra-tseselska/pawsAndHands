package com.pawsandhands.Adoption;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class AdoptionController {

    private AdoptionService adoptionService;
    private UserService userService;

    public AdoptionController(AdoptionService adoptionService, UserService userService) {
        this.adoptionService = adoptionService;
        this.userService = userService;
    }

    @GetMapping("/adoption")
    public String showAllEvents(Model model){

        try {
            model.addAttribute(
                    "adoptionPetList", adoptionService.findAllPetsForAdoption());
        }catch (Exception e){
            return "redirect:all-pets-adoption" + e.getMessage();  // this direction can be changed
        }

        return "all-pets-adoption";
    }
}
