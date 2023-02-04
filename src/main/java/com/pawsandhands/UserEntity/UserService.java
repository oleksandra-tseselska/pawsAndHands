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

    public ArrayList<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {return userRepository.findById(id); }

}
