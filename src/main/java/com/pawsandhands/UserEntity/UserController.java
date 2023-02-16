package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class UserController{

    private final UserService userService;

    private CustomMap<String, Value> modelUser = new CustomMap<>();

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping ("/logInStartPage")                                           //NEW  index.html + Btn"Login"
    public String handleUserLogin2(User user, HttpServletResponse response){             // first cookie save

        try{
            User loggedInUser = userService.verifyUser(user);
            setCookie(loggedInUser, response);

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){

            return "redirect:/?message=login_failed&error=" + e.getMessage();
        }
    }

    @GetMapping ("/profile-page/{userId}")                               //To add e-mail and password check
    public String showPage(Model model,
                           @CookieValue(value = "userId") String userIdFromCookie,
                           @PathVariable Long userId){
        try{
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model, userId);
            modelMap.values();
            modelMap.clear();

            return "profile-page";

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
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model);
            modelMap.values();
            modelMap.clear();

            return "profile-page";

        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    @GetMapping("/registration")                        //OK
    public String showRegistrationForm(){                  // if press Btn"Create new account" (?only after index.html)
        return "registration";                             // goes to registration.html
    }


    @PostMapping("/register")                                                      // after Btn "Register(?change to create)" in registration.html
    public String handleUserRegistration(User user, Model model) throws Exception {  //OK 2
        try {
            this.userService.createUser(user);
        }catch (Exception e){
            model.addAttribute("message", "signup_failed");     // ?we really need this in registration.html
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
            setCookie(loggedInUser, response);

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){
            return "redirect:registration-approval?message=login_failed&error=" + e.getMessage();

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

    @GetMapping("/update-profile")
    public String updateUser(Model model,
                             @CookieValue(value = "userId") String userIdFromCookie)
    {
        try{
            User userData = this.userService.findUserById(Long.valueOf(userIdFromCookie));
            model.addAttribute("userData", userData);

        }catch (Exception e){
            return "redirect:user?message=search_filed&error=" + e.getMessage();
        }

        return "user-update";
    }

    @PostMapping("/user-update")
    public String updateUserData (@ModelAttribute("userData") User user){

        try {
            this.userService.save(user);
        }catch (Exception e){
            return "redirect:user?message=update&error=" + e.getMessage();
        }

        return "redirect:profile-page/"+ user.getId();
    }

    private CustomMap<String, Value> getUserModelData(String stringUSERID, Model model) {
        try {
            Long userIdCookie = Long.valueOf(stringUSERID);
            User userData = this.userService.findUserById(userIdCookie);
            this.modelUser = putDataToModel(stringUSERID, model, userData);

        }catch (Exception e){
            e.getMessage();
        }

        return this.modelUser;
    }

    private CustomMap<String, Value> getUserModelData(String stringUSERID, Model model, Long USERID) {
        try {
            User userData = this.userService.findUserById(USERID);
            this.modelUser = putDataToModel(stringUSERID, model, userData);

        }catch (Exception e){
            e.getMessage();
        }

        return this.modelUser;
    }

//    Read about @NotNull
    private CustomMap<String, Value> putDataToModel(String stringUSERID, @NonNull Model model, @NonNull User userData){
        Long userIdCookie = Long.valueOf(stringUSERID);
        Set<Pet> userPet =  userData.getOwnedPets();

        this.modelUser.put("userData", model.addAttribute("userData", userData));
        this.modelUser.put("userPet", model.addAttribute("userPet", userPet));
        this.modelUser.put("userIdFromCookie", model.addAttribute("userIdFromCookie", userIdCookie));

        return  this.modelUser;
    }

    private void setCookie(User loggedInUser, HttpServletResponse response){
        Cookie cookie = new Cookie("userId", loggedInUser.getId().toString());
        response.addCookie(cookie);
        response.addCookie(new Cookie("userIsLoggedIn", "true"));
    }
}
