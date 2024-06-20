package com.example.bankapp.Transfer;

import java.math.BigDecimal;

public record TransferDto(
        Long idTransfer,
        Long sender,
        Long recipient,
        String title,
        String date,
        BigDecimal amount,
        Long idAccount
) {}