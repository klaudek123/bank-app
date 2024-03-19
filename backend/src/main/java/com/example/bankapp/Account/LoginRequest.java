package com.example.bankapp.Account;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
class LoginRequest {
    private Long idAccount;
    private String password;
}