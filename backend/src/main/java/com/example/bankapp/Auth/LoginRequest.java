package com.example.bankapp.Auth;


public record LoginRequest(
        Long idAccount,
        String password
){}
