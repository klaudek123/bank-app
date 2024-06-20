package com.example.bankapp.User;

import java.util.Date;

public record UserDto(
        Long personalId,
        String firstname,
        String lastname,
        Date dateOfBirth,
        String email,
        String address
){}
