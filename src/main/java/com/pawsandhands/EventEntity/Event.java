package com.pawsandhands.EventEntity;

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
    private String description;
    private String organizers;
    private long createdBy;         //userID of person who created event

    @CreationTimestamp
    private Timestamp createdAt;        //automatically puts data
    @UpdateTimestamp
    private Timestamp updatedAt;        //automatically puts data
}