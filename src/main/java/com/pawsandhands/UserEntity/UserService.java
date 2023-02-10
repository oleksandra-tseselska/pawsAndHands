package com.pawsandhands.UserEntity;

import com.pawsandhands.UserEntity.User;
import com.pawsandhands.UserEntity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired                                                   //dependency injection, constructor
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public void createUser(User user){
        this.userRepository.save(user);
    }

    public User verifyUser (User userLoginRequest) throws Exception {

        //Checking for a user in DB (email + password)
        User foundUser = this.userRepository.findUserByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());

        //If we don't find => we throw exception
        if(foundUser == null){
            throw new Exception("Username or password is incorrect");
        }

        //If we find it=> we return user info
        return foundUser;
    }


    public User findUserById(Long userId) throws Exception {
        return this.userRepository.findById(userId).orElseThrow();
    }



    public ArrayList<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {return userRepository.findById(id); }

}
