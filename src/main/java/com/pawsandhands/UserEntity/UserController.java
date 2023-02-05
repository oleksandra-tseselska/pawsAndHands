package com.pawsandhands.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/all-users")
    public String showUsers(Model model){
        System.out.println(this.userService.findAll());

        try{
            model.addAttribute("usersList", this.userService.findAll());
        }catch (Exception e){
            return "redirect:users?message=search_filed&error=" + e.getMessage();
        }

        return "all-users";
    }

    @GetMapping("/userUpdate/{userId}")
    public String updateUser(@PathVariable Integer userId,
                             Model model)
    {
        User user = userService.findById(userId);

        try{
            model.addAttribute("userData", userService.findById(userId));
        }catch (Exception e){
            return "redirect:user?message=search_filed&error=" + e.getMessage();
        }

        return "userUpdate";
    }

//    @PostMapping("/userUpdateBtn")
//    public String updateUserData (@RequestParam(name = "userId", required = false) Integer userId,
//                                  @RequestParam(name = "firstName", required = false) String firstName,
//                                  @RequestParam(name = "password", required = false) String password){
//
//        User user = userService.findById(userId);
//        System.out.println(userId);
//        System.out.println(firstName);
//        System.out.println(password);
//        System.out.println(user.toString());
//
//        if(!user.getName().equals(firstName)){
//
//        }
//        if(!user.getPassword().equals(password)){
//
//        }
//
//        return "redirect:users";
//    }

}
