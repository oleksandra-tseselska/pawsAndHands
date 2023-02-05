package com.pawsandhands.PetEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PetController {

//        private final UserService userService;

//        @Autowired
//        public UserController(UserService userService){
//            this.userService = userService;
//        }

        @GetMapping("/create-pet")
        public String showCreatePetPage(){
            return "create-pet"; //will find register in the Template folder. When we want to display the page
        }

    @GetMapping("/about-us")
    public String showAboutUsPage(){
        return "about-us"; //will find register in the Template folder. When we want to display the page
    }

//        @PostMapping("/createPet")
//        public String handlePetCreation(Pet pet){
//            try{
//                this.userService.createUser(user);
//
//            }
//            catch(Exception e){
//                return "redirect:/?message=signup_failed&error" + e.getMessage();
//
//            }
//            return "redirect:/?message=signup_success"; //when we want to send user to another endpoint
//        }
//
//        @GetMapping("/login")
//        public String showLoginPage(
//
//        ){
//            return "login"; //will find register in the Tempate folder. When we want to display the page
//        }
//    }


}
