package com.pawsandhands.PetEntity;

import com.pawsandhands.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.text.ParseException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Set;
import static java.time.temporal.ChronoUnit.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //represeting the db table in the db
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "petId")
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
    private Integer weight;
    private Integer height;
    private String furColor;
    private String eyeColor;
    private String mother;
    private String father;
    private String childs;
    private String events;
    private String medicalInfo;

    @ManyToMany(mappedBy = "ownedPets")
    private Set<User> petOwners;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public String petAge(String birthdate) throws ParseException {
        DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
        try {
            LocalDate datetime = LocalDate.parse(birthdate, f);
            System.out.println(datetime); // 2019-12-22
            int years = Math.toIntExact(datetime.until(LocalDate.now(), YEARS));
            if (years > 1) {
                return years + " years old";
            } else {
                int months = Math.toIntExact(datetime.until(LocalDate.now(), MONTHS));
                if (months > 1) {
                    return months + " months old";
                } else {
                    int weeks = Math.toIntExact(datetime.until(LocalDate.now(), WEEKS));
                    return weeks + " weeks old";
                }
            }
        } catch (DateTimeParseException e) {
            return null;
            // Exception handling message/mechanism/logging as per company standard
        }
    }
}
