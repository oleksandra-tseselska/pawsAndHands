package com.pawsandhands.EventEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)              //auto_increment for primary key
    private long id;
    private String name;
    private Date date;
    private String country;
    private String city;
    private String location;
    private String sponsors;
    private String description;
    private String organizers;
    private int createdBy;         //userID of person who created event
}
