package com.pawsandhands.Adoption;

import com.pawsandhands.EventEntity.Event;
import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class AdoptionController {

    private AdoptionService adoptionService;
    private UserService userService;

    public AdoptionController(AdoptionService adoptionService, UserService userService) {
        this.adoptionService = adoptionService;
        this.userService = userService;
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

//          //Setting again this adoption id (as it is not done manually by user)
            adoption.setId(petId);
            this.adoptionService.createPetForAdoption(adoption);

        } catch (Exception e) {
            return "redirect:edit-pet-for-adoption/" + e.getMessage();
        }
        return ("redirect:/adoption");
    }


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
            model.addAttribute("adoptionPetList", adoptionService.findAllPetsForAdoption());

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


}
