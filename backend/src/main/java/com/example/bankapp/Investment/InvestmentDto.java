package com.example.bankapp.Investment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentDto(
        Long idInvestment,
        String name,
        InvestmentType type,
        BigDecimal amount,
        BigDecimal interestRate,
        LocalDateTime startDate,
        LocalDateTime endDate,
        InvestmentStatus status
) {}