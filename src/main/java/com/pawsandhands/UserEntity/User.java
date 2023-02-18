package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //auto_increment for primary key
    @Column(name = "userId")
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
    @Column(length = 50000000)
    private byte[] photo;

    @Builder.Default
    private String photoPath = "/img/users-photo/profile-photo_placeholder.png";

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data

    @ManyToMany
    @JoinTable(
        name = "pets_owners",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
        inverseJoinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "petId"))
    private Set<Pet> ownedPets;

    public int getNumberOfPets(){
        return getOwnedPets().size();
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", surname='" + surname + '\'' +
//                ", email='" + email + '\'' +
//                ", status='" + status + '\'' +
//                ", breeder_licence='" + breeder_licence + '\'' +
//                ", country='" + country + '\'' +
//                ", city='" + city + '\'' +
//                ", telephone='" + telephone + '\'' +
//                ", password='" + password + '\'' +
//                ", photo=" + Arrays.toString(photo) +
//                ", photoPath=" + photoPath +
//                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
//                ", number of ownedPets=" + getNumberOfPets() +
//                '}';
//    }
}
