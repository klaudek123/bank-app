package com.example.bankapp.User;

import java.time.LocalDate;

public record UserDto(
        Long personalId,
        String firstname,
        String lastname,
        LocalDate dateOfBirth,
        String email,
        String address
){}
