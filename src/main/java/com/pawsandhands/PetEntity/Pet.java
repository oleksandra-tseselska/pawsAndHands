package com.pawsandhands.PetEntity;

import com.pawsandhands.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //represeting the db table in the db
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nickname;
    private String sex;
    private String type;
    private String breed;
    private String birthdate;
    private String country;
    private String city;
    private boolean lookingForDate;
    private boolean newHome;
    private boolean died;
    private String deathdate;
    private String fullName;
    private int weight;
    private int height;
    private String furColor;
    private String eyeColor;
    private String mother;
    private String father;
    private String childs;
    private String events;
    private String medicalInfo;
    @ManyToMany(mappedBy = "ownedPets")
    Set<User> petOwners;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
