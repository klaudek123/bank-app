package com.example.bankapp.User;

import java.time.LocalDate;


public record UserRegisterDto(
        Long personalId,
        String firstname,
        String lastname,
        LocalDate dateOfBirth,
        String email,
        String address,
        String password
) {}