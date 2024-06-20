package com.example.bankapp.Auth;

public interface AuthenticationService {
    boolean authenticate(Long idAccount, String password);
    String createToken(Long idAccount);
}
