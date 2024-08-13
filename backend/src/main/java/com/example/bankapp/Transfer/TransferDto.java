package com.example.bankapp.Transfer;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(
        Long idTransfer,
        Long sender,
        Long recipient,
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date,
        BigDecimal amount
) {}