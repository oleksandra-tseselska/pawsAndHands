package com.pawsandhands.PetEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/create-pet")
    public String showCreatePetPage(){
        return "create-pet"; //will find register in the Template folder. When we want to display the page
    }
    @PostMapping("/create-pet")
    public String handlePetCreation(Pet pet, Model model){
        try{
            this.petService.createPet(pet);
            System.out.println(pet);
            model.addAttribute("message", "signup_success");
        }
        catch(Exception e){
            model.addAttribute("message", "pet_profile_creation_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pet", pet);
            return "create-pet";

        }
        return "redirect:/create-pet?message=signup_success"; //when we want to send user to another endpoint
    }


}
