package com.pawsandhands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserService userService;
    private UserRepository repo;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping ("/profile-page")                               //To add e-mail and password check
    public String showPage(){
        return "profile-page";
    }
    @PostMapping ("/profile-page")                               //To add e-mail and password check
    public String showProfilePage(){
        return "redirect:profile-page";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(){
        return "registration";
    }

    @GetMapping("/registrationApproval")
    public String showRegistrationApproval(){
        return "registration-approval";
    }

    @PostMapping("/registrationApproval")
    public String handleUserRegistration(User user) throws Exception {
        try {
            this.userService.createUser(user);
        }catch (Exception e){
            return "redirect:registration" + e.getMessage();
        }

        //Does not work: registration-approval?message=signupsuccess
        return ("registration-approval");

    }


}
