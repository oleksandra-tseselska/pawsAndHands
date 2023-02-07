package com.pawsandhands.UserEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository <User,Long> {
    User findById(Integer id);  //Below is method with type Long, not Int

    @Override
    ArrayList<User> findAll();

    User findUserByEmailAndPassword(String email, String password); //For login purposes, searching in User table

    User findUserById(Long userId);

}
