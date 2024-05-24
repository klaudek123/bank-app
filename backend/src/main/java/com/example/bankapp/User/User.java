package com.example.bankapp.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "personal_id")
    private Long personalId;

    @Column(name = "firstname", nullable = false, length = 30)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 30)
    private String lastname;

    @Column(name = "date_of_birth", nullable = false, columnDefinition = "DATE")
    private Date dateOfBirth;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "address", length = 40)
    private String address;
}
