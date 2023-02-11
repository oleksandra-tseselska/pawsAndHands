package com.pawsandhands.UserEntity;

import com.pawsandhands.EventEntity.Event;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class UserController {

    private UserService userService;
    private UserRepository repo;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }


    @GetMapping ("/profile-page/{userId}")                               //To add e-mail and password check
    public String showPage(@PathVariable Long userId, Model model,
                           @CookieValue(value = "userId") String userIdFromCookie){
        try{
            System.out.println(userIdFromCookie);
            model.addAttribute("userData", userService.findUserById(Long.valueOf(userIdFromCookie)));
//            return "profile-page";
                return "viewUserInfo";

        }catch (Exception e){
            return "redirect:/?message=user_not_found";
        }
    }

    @GetMapping("/profile")
    public String showMyEvents(Model model, @CookieValue(value = "userId") String userIdFromCookie){
        try {
            User userData = this.userService.findUserById(Long.valueOf(userIdFromCookie));
            model.addAttribute("userData", userData);

            return "viewUserInfo";

        }catch (Exception e){
            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    @GetMapping("/registration")                        //OK
    public String showRegistrationForm(){
        return "registration";
    }


    @PostMapping("/register")
    public String handleUserRegistration(User user, Model model) throws Exception {  //OK 2
        try {
            this.userService.createUser(user);
        }catch (Exception e){
            model.addAttribute("message", "signup_failed");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "registration";
        }
        return ("redirect:registration-approval?message=signup_success");
    }

    @GetMapping("/registration-approval")                                      //OK 3
    public String showRegistrationApprovalLoginPage(
            @RequestParam(name = "message", required = false) String message,
            Model model
    ){
        model.addAttribute("message", message);
        return "registration-approval";
    }


    @PostMapping ("/registration-approval")                               //OK 4
    public String handleUserLogin(User user, HttpServletResponse response){

        try{
            User loggedInUser = userService.verifyUser(user);

            Cookie cookie = new Cookie("userId", loggedInUser.getId().toString());
            response.addCookie(cookie);
            response.addCookie(new Cookie("userIsLoggedIn", "true"));

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){
            return "redirect:registration-approval?message=login_failed&error=" + e.getMessage();

        }
    }


    @GetMapping("/logInStartPage")                        //NEW
    public String showLoggedInMode(){
        return "profile-page";
    }

    @PostMapping ("/logInStartPage")                                           //NEW
    public String handleUserLogin2(User user, HttpServletResponse response){

        try{
            User loggedInUser = userService.verifyUser(user);

            Cookie cookie = new Cookie("userId", loggedInUser.getId().toString());
            response.addCookie(cookie);
            response.addCookie(new Cookie("userIsLoggedIn", "true"));

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){
            return "redirect:/?message=login_failed&error=" + e.getMessage();

        }

    }


    @GetMapping("/all-users")
    public String showUsers(Model model){

        try{
            model.addAttribute("usersList", this.userService.findAll());
        }catch (Exception e){
            return "redirect:users?message=search_filed&error=" + e.getMessage();
        }

        return "all-users";
    }



    @GetMapping("/viewUserInfo/{userId}")
    public String viewUserInfo(@PathVariable Integer userId,
                             Model model)
    {
        User user = userService.findById(userId);

        try{
            model.addAttribute("userData", userService.findById(userId));
        }catch (Exception e){
            return "redirect:user?message=search_filed&error=" + e.getMessage();
        }

        return "viewUserInfo";
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

    @PostMapping("/userUpdateBtn")
    public String updateUserData (@RequestParam(name = "userId", required = false) Integer userId,
                                  @RequestParam(name = "firstName", required = false) String firstName,
                                  @RequestParam(name = "password", required = false) String password){

        User user = userService.findById(userId);
        System.out.println(userId);
        System.out.println(firstName);
        System.out.println(password);
        System.out.println(user.toString());

        if(!user.getName().equals(firstName)){

        }
        if(!user.getPassword().equals(password)){

        }

        return "redirect:users";
    }

}
