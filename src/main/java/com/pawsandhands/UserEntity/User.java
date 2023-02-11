package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
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

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)              //auto_increment for primary key
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String status;
    private String breeder_licence;
    private String country;
    private String city;
    private String telephone;
    private String password;
    //boolean isAdmin;
    @Lob                               //annotation for BLOB format in DB; B64image Spring - saving like String
    private byte[] photo;

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data

    @ManyToMany
    @JoinTable(
        name = "pets_owners",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private Set<Pet> ownedPets;
}
