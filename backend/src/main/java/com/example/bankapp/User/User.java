package com.example.bankapp.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    private Long personalId;

    private String firstname;
    private String lastname;
    @Column(columnDefinition = "DATE")
    private Date dateOfBirth;
    private String email;
    private String address;
}
