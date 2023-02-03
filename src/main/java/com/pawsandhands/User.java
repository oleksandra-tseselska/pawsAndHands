package com.pawsandhands;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)              //auto_increment for primary key
    private long id;
    private String name;
    private String surname;
    private String email;
    private String status;
    private String breeder_licence;
    private String country;
    private String city;
    private String telephone;
    private String password;
    //boolean isAdmin;
//    @Lob                               //annotation for BLOB format in DB; B64image Spring - saving like String
//    private Byte[] photo;

     private String photo;

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data
}
