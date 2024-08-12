package com.example.bankapp.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(
        Long idTransfer,
        Long sender,
        Long recipient,
        String title,
        LocalDateTime date,
        BigDecimal amount
) {}