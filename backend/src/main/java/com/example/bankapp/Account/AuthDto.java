package com.example.bankapp.Account;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthDto {
    private Long idAccount;
    private String token;

}
