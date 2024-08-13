package com.example.bankapp.Investment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentDto(
        Long idInvestment,
        String name,
        InvestmentType type,
        BigDecimal amount,
        BigDecimal interestRate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
        InvestmentStatus status
) {}