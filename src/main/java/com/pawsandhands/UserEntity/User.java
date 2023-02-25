package com.pawsandhands.UserEntity;

import com.pawsandhands.PetEntity.Pet;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.net.URL;
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
    boolean isAdmin = false;
    private URL facebookUrl;
    private URL twitterUrl;
    private URL instagramUrl;
    private URL youtubeUrl;
    private URL pinterestUrl;
//    @Lob                               //annotation for BLOB format in DB; B64image Spring - saving like String
    @Column(columnDefinition = "LONGTEXT")
    private String photo;
    @Builder.Default
    private String photoPath = "/img/users-photo/profile-photo_placeholder.png";

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data

    @ManyToMany(mappedBy = "petOwners")
    private Set<Pet> ownedPets;

    //    @OneToMany(mappedBy="userId")
//    private Set<Message> messagesSent;

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
