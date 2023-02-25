package com.pawsandhands.UserEntity;

import com.pawsandhands.FileStorage.FilesStorageServiceImpl;
import com.pawsandhands.PetEntity.Pet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

@Controller
public class UserController{

    private final UserService userService;

    private final FilesStorageServiceImpl filesStorage;
    private CustomMap<String, Value> modelUser = new CustomMap<>();

    private final Path imgUsersPath = Paths.get("src/main/resources/static/img/users-photo");

    @Autowired
    public UserController(UserService userService, FilesStorageServiceImpl filesStorage){
        this.userService=userService;
        this.filesStorage = filesStorage;
    }


    @GetMapping("/log-out")
    public String handleUserLogOut(HttpServletResponse response){

        try {
            Cookie cookie = new Cookie("userId", null);
            cookie.setMaxAge(0);                                     // Сookie expired
            response.addCookie(cookie);

            response.addCookie(new Cookie("userIsLoggedIn", "false"));

            return "redirect:/";
        }catch (Exception e){
            return "redirect:/?message=logout_failed&error=" + e.getMessage();
        }

    }


//
//    @GetMapping("/")
//        public String showMainPage(Model model){
//            return "index";
//        }



    // TO CHECK HERE

    @PostMapping ("/logInStartPage")                                           //NEW  index.html + Btn"Login"
    public String handleUserLogin2(User user, Model model, HttpServletResponse response){

        //model.addAttribute("message1", "login_success");

        try{
            User loggedInUser = userService.verifyUser(user);
            setCookie(loggedInUser, response);                     // first cookie save

//            Add ADMIN rights to UserID == 1

            if(loggedInUser.getId() == 1 && !loggedInUser.isAdmin()){
                loggedInUser.setAdmin(true);
                this.userService.save(loggedInUser);
            }

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){
            model.addAttribute("message",  "login_failed");
            return "redirect:/login-page";
        }
    }

    @GetMapping ("/profile-page/{userId}")                               //To add e-mail and password check
    public String showPage(Model model,
                           @CookieValue(value = "userId") String userIdFromCookie,
                           @PathVariable Long userId){
        try{
//            delete temp img
            this.filesStorage.deleteAll();

//          get photo from DB
            User currentUser = this.userService.findUserById(userId);

            if(currentUser.getPhoto() != null){
                String pathFileUser = "profile_photo_" +currentUser.getId().toString()+ ".png";
                this.filesStorage.base64DecodedString(currentUser.getPhoto(), pathFileUser, imgUsersPath);
            }

//          send data to html
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model, userId);
            modelMap.values();
            modelMap.clear();

            User user = userService.findUserById(Long.valueOf(userIdFromCookie)); //fining User in our DB

//            check admin
            if (user.isAdmin) {
                model.addAttribute("currentUserIsAdmin", true);
            }else{
                model.addAttribute("currentUserIsAdmin", false);
            }

            return "profile-page";

        }catch (Exception e){
            model.addAttribute("message",  "login_failed");
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
//            delete temp img
            this.filesStorage.deleteAll();

//          get photo from DB
            User user = findUserByCookieId(userIdFromCookie);

            if(user.getPhoto() != null){
                String pathFileUser = "profile_photo_" +user.getId().toString()+ ".png";
                this.filesStorage.base64DecodedString(user.getPhoto(), pathFileUser, imgUsersPath);
            }

//          send data to html
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model);
            modelMap.values();
            modelMap.clear();

            return "profile-page";

        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }
    @GetMapping ("/edit-user-photo/{userId}")
    public String showProfilePhoto(Model model,
                                   @CookieValue(value = "userId") String userIdFromCookie
    ){
        try {
//            delete temp img
            this.filesStorage.deleteAll();

//          send data to html
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model);
            modelMap.get("userData");
            modelMap.clear();

            return "edit-user-photo";

        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }
    @PostMapping(value = "/add-photo")
    public String editProfilePhoto(@RequestParam("photo") MultipartFile multipartFile,
                                   @CookieValue(value = "userId") String userIdFromCookie){
        try{
//            add folder for temp photos
            this.filesStorage.init();

            User user = findUserByCookieId(userIdFromCookie);
            String pathFileUser;
            String pathUserPhoto;
            Path userPhotoPath;

                pathFileUser = "profile_photo_" +user.getId().toString()+ ".png";
                pathUserPhoto = "/img/users-photo/profile_photo_"+user.getId()+ ".png";

//            file to Base64 and save
            userPhotoPath = this.imgUsersPath.resolve(Paths.get(pathFileUser));
            String encodedString = this.filesStorage.base64EncodedString(multipartFile);
            this.filesStorage.save(multipartFile, userPhotoPath, pathFileUser);

//            send data to DB
            user.setPhoto(encodedString);
            user.setPhotoPath(pathUserPhoto);

            this.userService.save(user);
            this.filesStorage.deleteAll();

            return "redirect:spinner-user";

        }catch (Exception e){

            return "redirect:error-img-page";
        }
    }

    @GetMapping ("/error-img-page")
    public String errorEditProfilePhoto(@CookieValue(value = "userId") String userIdFromCookie, Model model){
        try {
//          send data to html
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model);
            modelMap.get("userData");
            modelMap.clear();

            return "error-img-page";

        }catch (Exception e){

            return "redirect:error-img-page?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }
    @GetMapping ("/spinner-user")
    public String showSpinnerUser(@CookieValue(value = "userId") String userIdFromCookie,
                                   Model model){
        try {
//          send data to html
            CustomMap<String, Value> modelMap = getUserModelData(userIdFromCookie, model);
            modelMap.values();
            modelMap.clear();

            return "spinner-user";

        }catch (Exception e){

            return "redirect:index?message=update_error" + e.getMessage();          //Endpoint can be changed !!!
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
        return ("redirect:login-page?message=signup_success");
    }

    @GetMapping("/login-page")                                      //OK 3
    public String showRegistrationApprovalLoginPage(
            @RequestParam(name = "message", required = false) String message,
            Model model
    ){
//        if(message == null){
//            return "redirect:registration";
//        }

        model.addAttribute("message", message);
        return "login-page";
    }


    @PostMapping ("/login-page")                               //OK 4
    public String handleUserLogin(User user, HttpServletResponse response){

        try{
            User loggedInUser = userService.verifyUser(user);
            setCookie(loggedInUser, response);

            return "redirect:profile-page/" + loggedInUser.getId();

        }catch(Exception e){
            return "redirect:login-page?message=login_failed&error=" + e.getMessage();

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

//        get photos of all users
        try{
            ArrayList<User> users = this.userService.findAll();
            for(User user: users){
                if(user.getPhoto() != null){
                    String pathFileUser = "profile_photo_" +user.getId().toString()+ ".png";
//                    String pathFileUser = "profile_photo_" +user.getId().toString()+ "." +user.getContentType();
                    this.filesStorage.base64DecodedString(user.getPhoto(), pathFileUser, imgUsersPath);
                }
            }

            model.addAttribute("usersList", users);
            model.addAttribute("userIdFromCookie", Long.valueOf(userIdFromCookie));


            User user = userService.findUserById(Long.valueOf(userIdFromCookie)); //fining User in our DB

            if (user.isAdmin) {
                model.addAttribute("currentUserIsAdmin", true);
            }

        }catch (Exception e){
            return "redirect:users?message=search_filed&error=" + e.getMessage();
        }

        return "all-users";
    }

    //HERE HERE HERE

    @PostMapping("/grantAdminRights")
    public String grantAdminRights(
            @RequestParam(name = "userIdToBeAdmin", required = false) Long userIdToBeAdmin,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println("ID of User that will be granted admin rights: " + userIdToBeAdmin);
            User userToBeAdmin = userService.findUserById(Long.valueOf(userIdToBeAdmin));
            userToBeAdmin.setAdmin(true);
            userService.createUser(userToBeAdmin);
        }catch (Exception e){
            return "redirect:/all-users" + e.getMessage();
        }
        return "redirect:/all-users";
    }


    @PostMapping("/takeOffAdminRights")
    public String takeOffAdminRights(
            Model model,
            @RequestParam(name = "userIdNotToBeAdmin", required = false) Long userIdNotToBeAdmin,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println("ID of User that will be taken off admin rights: " + userIdNotToBeAdmin);
            User userNotToBeAdmin = userService.findUserById(Long.valueOf(userIdNotToBeAdmin));
            userNotToBeAdmin.setAdmin(false);
            userService.createUser(userNotToBeAdmin);
        }catch (Exception e){
            return "redirect:/all-users" + e.getMessage();
        }
        return "redirect:/all-users";
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

    private User findUserByCookieId(String stringUSERID) throws Exception{
        Long userIdCookie = Long.valueOf(stringUSERID);
        User userData = this.userService.findUserById(userIdCookie);

        return userData;
    }

    private CustomMap<String, Value> getUserModelData(String stringUSERID, Model model) {
        try {
            this.modelUser = putDataToModel(stringUSERID, model, findUserByCookieId(stringUSERID));

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
        cookie.setMaxAge(7 * 24 * 60 * 60); // Сookie expires in 7 days
        response.addCookie(cookie);
        response.addCookie(new Cookie("userIsLoggedIn", "true"));
    }
}
