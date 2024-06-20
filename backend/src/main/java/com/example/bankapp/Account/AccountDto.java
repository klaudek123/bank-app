package com.example.bankapp.Account;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDto (
        Long number,
        BigDecimal balance,
        LocalDateTime dateOfCreation,
        String status // 1 = active, 0 = inactive
){}