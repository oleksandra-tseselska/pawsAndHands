package com.pawsandhands.UserEntity;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.PetEntity.Pet;
import com.pawsandhands.PetEntity.PetController;
import com.pawsandhands.PetEntity.PetService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private UserRepository repo;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping ("/profile-page/{userId}")                               //To add e-mail and password check
    public String showPage(Model model,
                           @CookieValue(value = "userId") String userIdFromCookie){
        try{
            User userData = this.userService.findUserById(Long.valueOf(userIdFromCookie));
            Set<Pet> userPet =  userData.getOwnedPets();

            model.addAttribute("userData", userData);
            model.addAttribute("userPet", userPet);

//            return "profile-page";
                return "viewUserInfo";

        }catch (Exception e){
            return "redirect:/?message=user_not_found";
        }
    }

    @GetMapping("/profile")
    public String showMyEvents(Model model,
           @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
           @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        try {
            User userData = this.userService.findUserById(Long.valueOf(userIdFromCookie));
            Set<Pet> userPet =  userData.getOwnedPets();
            model.addAttribute("userData", userData);
            model.addAttribute("userPet", userPet);
            model.addAttribute("userIdFromCookie", Long.valueOf(userIdFromCookie));

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
        if(message == null){
            return "redirect:registration";
        }
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
    public String showUsers(Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        try{
            model.addAttribute("usersList", this.userService.findAll());
            model.addAttribute("userIdFromCookie", Long.valueOf(userIdFromCookie));
        }catch (Exception e){
            return "redirect:users?message=search_filed&error=" + e.getMessage();
        }

        return "all-users";
    }



    @GetMapping("/viewUserInfo/{userId}")
    public String viewUserInfo(@PathVariable Integer userId, Model model) {

        User user = userService.findById(userId);
        Set<Pet> userPet =  user.getOwnedPets();

        try{
            model.addAttribute("userData", userService.findById(userId));
            model.addAttribute("userPet", userPet);

        }catch (Exception e){
            return "redirect:user?message=search_filed&error=" + e.getMessage();
        }

        return "viewUserInfo";
    }


    @GetMapping("/update-profile/{userId}")
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
