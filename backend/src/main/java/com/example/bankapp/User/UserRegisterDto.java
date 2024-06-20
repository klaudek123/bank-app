package com.example.bankapp.User;

import java.util.Date;

public record UserRegisterDto(
        Long personalId,
        String firstname,
        String lastname,
        Date dateOfBirth,
        String email,
        String address,
        String password
) {}