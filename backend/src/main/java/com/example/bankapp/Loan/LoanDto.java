package com.example.bankapp.Loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanDto(
        Long idLoan,
        BigDecimal amount,
        BigDecimal interestRate,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LoanStatus status
) {}
