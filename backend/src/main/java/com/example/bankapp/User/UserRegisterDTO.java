package com.example.bankapp.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private Long personalId;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String email;
    private String address;
    private String password;

}