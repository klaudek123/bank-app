package com.example.bankapp.Loan;

import lombok.*;

import java.math.BigDecimal;

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
