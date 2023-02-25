package com.pawsandhands.Adoption;

import com.pawsandhands.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)              //auto_increment for primary key
    private long id;
    private String name;
    private String age;
    private String type;//cat or dog
    private String sex;
    private String shelter;
    private String description;          //In html card => name, age, shelter, short desc, button "Reserve" + badge with type
    private boolean reserved = false;

//    @Lob                               //annotation for BLOB format in DB; B64image Spring - saving like String
//    private byte[] photo;
    @Column(columnDefinition = "LONGTEXT")
    private String photo;

    @Builder.Default
    private String photoPath = "/img/adoption-photo/adoption_placeholder.png";
    @ManyToOne
    @PrimaryKeyJoinColumn(name="UserID", referencedColumnName="id")         //one User can adopt many pets
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data
}
