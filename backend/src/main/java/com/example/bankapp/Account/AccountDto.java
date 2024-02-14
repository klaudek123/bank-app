package com.example.bankapp.Account;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long number;
    private Long balance;
    private LocalDateTime dateOfCreation;
    private String status; // 1 = active, 0 = unactive
}
