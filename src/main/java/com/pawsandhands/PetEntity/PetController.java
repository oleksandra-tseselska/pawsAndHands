package com.pawsandhands.PetEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PetController {

//        private final PetService petService;

//        @Autowired
//        public UserController(UserService userService){
//            this.userService = userService;
//        }

        @GetMapping("/create-pet")
        public String showCreatePetPage(){
            return "create-pet"; //will find register in the Template folder. When we want to display the page
        }
//        @PostMapping("/create-pet")
//        public String handlePetCreation(Pet pet){
//            try{
//                this.petService.createUser(pet);
//
//            }
//            catch(Exception e){
//                return "redirect:/?message=pet_profile_creation_failed&error" + e.getMessage();
//
//            }
//            return "redirect:/?message=signup_success"; //when we want to send user to another endpoint
//        }


}
