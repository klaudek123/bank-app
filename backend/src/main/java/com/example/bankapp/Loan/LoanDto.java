package com.example.bankapp.Loan;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanDto(
        Long idLoan,
        BigDecimal amount,
        BigDecimal interestRate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
        LoanStatus status
) {}
