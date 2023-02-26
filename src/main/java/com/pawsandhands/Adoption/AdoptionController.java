package com.pawsandhands.Adoption;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.FileStorage.FilesStorageServiceImpl;
import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class AdoptionController {

    private AdoptionService adoptionService;
    private UserService userService;
    private final FilesStorageServiceImpl filesStorage;
    private final Path imgAdoptionsPath = Paths.get("src/main/resources/static/img/adoption-photo");

    public AdoptionController(AdoptionService adoptionService, UserService userService, FilesStorageServiceImpl filesStorage) {
        this.adoptionService = adoptionService;
        this.userService = userService;
        this.filesStorage = filesStorage;
    }

    @GetMapping("/addPetForAdoption")
    public String showPetsAdoptionForm(
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }
        return "create-pet-for-adoption";
    }


    //To change return location
    @GetMapping("/createPetForAdoption")
    public String handlePetCreation(){
        return ("create-pet-for-adoption");
    }

    //To change return location
    @PostMapping("/createPetForAdoption")
    public String handlePetCreation(Adoption adoption,
                                      @CookieValue(value = "userId") String userIdFromCookie
    ) throws Exception {
        try {
            this.adoptionService.createPetForAdoption(adoption);
        } catch (Exception e) {
            return "redirect:create-pet-for-adoption" + e.getMessage();
        }
        return ("redirect:/adoption");    //check
    }


    //DELETE PET FOR ADOPTION

    @PostMapping("/deletePetForAdoption")
    public String deletePetForAdoption(
            Model model,
            @RequestParam(name = "petIdToBeDeleted", required = false) Long petIdToBeDeleted,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println("Pet with following id was deleted: " + petIdToBeDeleted);
            this.adoptionService.deletePetForAdoption(petIdToBeDeleted);

        }catch (Exception e){
            return "redirect:all-pets-adoption?message=search_filed&error=" + e.getMessage();          //Endpoint can be changed !!!
        }
        return "redirect:/adoption";
    }


    //EDIT PET FOR ADOPTION

    @GetMapping("/editPetForAdoption")                                        //smotretj anketu zapolnennuju
    public String updatePetView(
            Model model,
            @RequestParam(name = "petIdToBeUpdated", required = false) Long petIdToBeUpdated,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            model.addAttribute("pet",adoptionService.findAdoptionPetById(petIdToBeUpdated));
            System.out.println("Pet with the following ID will be updated: " + petIdToBeUpdated);

        }catch (Exception e){
            return "redirect:create-pet-for-adoption?message=search_filed&error=" + e.getMessage();          //Endpoint can be changed !!!
        }
        return "edit-pet-for-adoption";}


    @PostMapping("/savePetForAdoption")
    public String handlePetUpdate(Adoption adoption,
                                    @RequestParam(name = "petId", required = false) Long petId,
                                    @CookieValue(value = "userId") String userIdFromCookie
    ) throws Exception {
        try {
            Adoption oldAdoptionData = adoptionService.findAdoptionPetById(petId);

            //Setting creation date info (as it is not done manually by user)
            adoption.setCreatedAt(oldAdoptionData.getCreatedAt());
            adoption.setPhoto(oldAdoptionData.getPhoto());
            adoption.setPhotoPath(oldAdoptionData.getPhotoPath());


//          //Setting again this adoption id (as it is not done manually by user)
            adoption.setId(petId);
            this.adoptionService.createPetForAdoption(adoption);

        } catch (Exception e) {
            return "redirect:edit-pet-for-adoption/" + e.getMessage();
        }
        return ("redirect:/adoption");
    }

    //   Add Photo Start
    @GetMapping ("/editAdoptionPhoto")
    public String showPetPhoto(Model model,
                               @RequestParam(name = "petIdToBeUpdated", required = false) Long petIdToBeUpdated
    ){
        try {
//            delete temp img
            this.filesStorage.deleteAll();
//            data from DB
            model.addAttribute("adoption", adoptionService.findAdoptionPetById(petIdToBeUpdated));

            return "edit-adoption-pet-photo";
        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    @PostMapping("/addPhotoAdoptionPet")
    public String editEventPhoto(@RequestParam(name = "petIdToBeUpdated", required = false) Long petIdToBeUpdated,
                                 @RequestParam("photo") MultipartFile multipartFile){
        try{
//            add folder for temp photos
            this.filesStorage.init();

            Adoption adoptionPet = this.adoptionService.findAdoptionPetById(petIdToBeUpdated);
            String pathFileAdoptionPet;
            String pathAdoptionPetPhoto;
            Path eventPhotoPath;

            pathFileAdoptionPet = "adoption-pet_photo_" +petIdToBeUpdated.toString()+ ".png";
            pathAdoptionPetPhoto = "/img/adoption-photo/adoption-pet_photo_"+petIdToBeUpdated+ ".png";


//            file to Base64 and save
            eventPhotoPath = this.imgAdoptionsPath.resolve(Paths.get(pathFileAdoptionPet));
            String encodedString = this.filesStorage.base64EncodedString(multipartFile);
            this.filesStorage.save(multipartFile, eventPhotoPath, pathFileAdoptionPet);

//            send data to DB
            adoptionPet.setPhoto(encodedString);
            adoptionPet.setPhotoPath(pathAdoptionPetPhoto);
            this.adoptionService.createPetForAdoption(adoptionPet);

        }catch (Exception e){
            e.getMessage();
        }

        return "redirect:/spinner-adoption/"+petIdToBeUpdated.toString();
    }

    @GetMapping ("/spinner-adoption/{adoptionPetId}")
    public String showSpinnerPet(){
        try {
            return "spinner-adoption";
        }catch (Exception e){

            return "redirect:index?message=profile_error" + e.getMessage();          //Endpoint can be changed !!!
        }
    }

    //   Add Photo End


    //I AM HERE

    @GetMapping("/adoption")
    public String showAllPetsForAdoption(
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            //            delete temp img
            this.filesStorage.deleteAll();

            ArrayList<Adoption> allAdoptions = adoptionService.findAllPetsForAdoption();

            //     get photos of all adoption pets from DB
            for(Adoption adoption: allAdoptions){
                if(adoption.getPhoto() != null){
                    String pathFileUser = "adoption-pet_photo_" +adoption.getId()+ ".png";
                    this.filesStorage.base64DecodedString(adoption.getPhoto(), pathFileUser, imgAdoptionsPath);
                }
            }

            model.addAttribute("adoptionPetList", allAdoptions);

            User user = userService.findUserById(Long.valueOf(userIdFromCookie)); //fining User in our DB

            //Checking if User is Admin, if yes => in html additional button will appear => to add pet
            if (user.isAdmin()) {
                model.addAttribute("currentUserIsAdmin", true);
            }

            return "all-pets-adoption";


        } catch (Exception e) {
            return "redirect:all-pets-adoption" + e.getMessage();  // this direction can be changed
        }

    }


    @GetMapping("/my-pets-for-adoption")                //to add ref to this endpoint
    public String showMyPetsForAdoption(
            Model model,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println(userIdFromCookie);
            User userWhoAdoptsPets = userService.findUserById(Long.valueOf(userIdFromCookie));

            ArrayList<Adoption> myAdoptions = this.adoptionService.findReservedPetsByUser(userWhoAdoptsPets);

            //     get photos of all adoption pets from DB
            for(Adoption adoption: myAdoptions){
                if(adoption.getPhoto() != null){
                    String pathFileUser = "adoption-pet_photo_" +adoption.getId()+ ".png";
                    this.filesStorage.base64DecodedString(adoption.getPhoto(), pathFileUser, imgAdoptionsPath);
                }
            }

            model.addAttribute("myAdoptionList", myAdoptions);
            return "my-adoptions";     //create this html

        } catch (Exception e) {
            return "redirect:all-pets-adoption" + e.getMessage();  // this direction can be changed
        }
    }


    @GetMapping("/reserve")
    public String reservePetPage(){
        return "reservation-approval";
    }

    @PostMapping("/reserve")
    public String reservePet(
           // @PathVariable Long petId,
            Model model,
            @RequestParam(name = "petId", required = false) Long petId,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            System.out.println(userIdFromCookie);
            User userWhoReservesPet = userService.findUserById(Long.valueOf(userIdFromCookie));
            Adoption reservedPet = adoptionService.findAdoptionPetById(petId);

            if(reservedPet.isReserved()==false){
                reservedPet.setUser(userWhoReservesPet);
                reservedPet.setReserved(true);
                adoptionService.createPetForAdoption(reservedPet);
                return "reservation-approval";
            }else{
                return "reservation-refusal";
            }
        }catch (Exception e){
            return "redirect:/adoption" + e.getMessage();          //Endpoint can be changed !!!
        }
    }


    //UNRESERVE PET


    @GetMapping("/takeOffReservation")
    public String unreservePetPage(){
        return "reservation-takeoff";
    }

    @PostMapping("/takeOffReservation")
    public String unreservePet(
            Model model,
            @RequestParam(name = "petIdToBeUnreserved", required = false) Long petId,
            @CookieValue(value = "userId", defaultValue = "noId") String userIdFromCookie ,
            @CookieValue(value="userIsLoggedIn", defaultValue = "false") String userIsLoggedInFromCookie //extracts cookie value from the browser
    ){
        if(userIsLoggedInFromCookie.equals("false")) {
            return "not-logged-in";
        }

        try {
            Adoption reservedPet = adoptionService.findAdoptionPetById(petId);

            reservedPet.setUser(null);
            reservedPet.setReserved(false);
            adoptionService.createPetForAdoption(reservedPet);
            } catch (Exception e){
            return "redirect:reservation-takeoff" + e.getMessage();          //Endpoint can be changed !!!
        }

        return "reservation-takeoff";
    }





}
