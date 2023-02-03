package com.pawsandhands.UserEntity;

import com.pawsandhands.UserEntity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <User,Long> {

User findByEmail(String email);     //SQL query to find user by e-mail
}
