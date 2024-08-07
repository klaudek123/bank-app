package com.example.bankapp.Account;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public record AccountDto(
        Long idAccount,
        Long number,
        BigDecimal balance,
        LocalDateTime dateOfCreation,
        String type,
        String status // 1 = active, 0 = inactive
) {}