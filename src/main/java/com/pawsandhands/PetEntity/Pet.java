package com.pawsandhands.PetEntity;

import com.pawsandhands.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import java.time.format.FormatStyle;
import java.util.Set;
import static java.time.temporal.ChronoUnit.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    @Column(columnDefinition = "LONGTEXT")
    private String photo;
    private String contentType;

    @Builder.Default
    private String photoPath = "/img/pets-photo/pet_placeholder.png";

    @ManyToMany
    @JoinTable(
            name = "pets_owners",
            joinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "petId"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"))
    private Set<User> petOwners;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public String petAge(Pet pet) throws ParseException {
        String birthdate = pet.getBirthdate();
        DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
        LocalDate endOflIfe = LocalDate.now();

        if (pet.isDied() && !pet.getDeathdate().isBlank()) {
            endOflIfe = LocalDate.parse(pet.getDeathdate(), f);
        }

        try {
            LocalDate birthdateParsed = LocalDate.parse(birthdate, f);
//            System.out.println(birthdateParsed); // 2019-12-22

                int years = Math.toIntExact(birthdateParsed.until(endOflIfe, YEARS));
                if (years > 1) {
                    return years + " years old";
                } else {
                    int months = Math.toIntExact(birthdateParsed.until(endOflIfe, MONTHS));
                    if (months > 1) {
                        return months + " months old";
                    } else {
                        int weeks = Math.toIntExact(birthdateParsed.until(endOflIfe, WEEKS));
                        return weeks + " weeks old";
                    }
                }
            } catch(DateTimeParseException e){
                return null;
                // Exception handling message/mechanism/logging as per company standard
            }
    }

    public String dateFormatter(String date) throws ParseException {
        DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
        LocalDate dateParsed = LocalDate.parse(date, f);

        return dateParsed.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

    }
}
