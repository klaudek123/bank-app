package com.example.bankapp.Loan;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanDto {
    private Long idLoan;
    private BigDecimal amount;
    private Long idAccount;
}
