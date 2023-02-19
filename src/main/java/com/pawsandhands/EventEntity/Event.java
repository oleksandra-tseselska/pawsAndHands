package com.pawsandhands.EventEntity;

import com.pawsandhands.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)              //auto_increment for primary key
    private long id;
    private String name;
    private String date;
    private String country;
    private String city;
    private String location;
    private String sponsors;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String organizers;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="USERID", referencedColumnName="id")
    private User user;

//    @Lob                               //annotation for BLOB format in DB; B64image Spring - saving like String
//    @Column(length = 50000000)
//    private byte[] photo;
//
//    @Builder.Default
//    private String photoPath = "/img/events-photo/event-photo_placeholder.png";

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data

}
