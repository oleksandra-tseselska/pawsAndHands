package com.pawsandhands.UserEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Modifying
    @Query("update User u set u.photo = ?1, u.photoPath = ?2 where u.id = ?3")
    void setUserInfoById(byte[] photo, String photoPath, Long userId);

//    @Override
//    <S extends User> S save(S entity);
}
