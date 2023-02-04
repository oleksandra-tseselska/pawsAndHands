package com.pawsandhands.UserEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository <User,Long> {
    User findById(Integer id);

    @Override
    ArrayList<User> findAll();

    User findByEmail(String email);     //SQL query to find user by e-mail
}
