package com.example.bankapp.Loan;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class LoanDto {
        private Long idLoan;
        private BigDecimal amount;
        private BigDecimal interestRate;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String status;
        private Long idAccount;
    }
