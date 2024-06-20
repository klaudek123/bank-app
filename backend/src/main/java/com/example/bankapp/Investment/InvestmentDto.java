package com.example.bankapp.Investment;

import com.example.bankapp.Account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvestmentDto {
    private Long idInvestment;
    private String name;

    @Enumerated(EnumType.STRING)
    private InvestmentType type;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private String startDate;

    @Column(columnDefinition = "DATESTAMP")
    private String endDate;

    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;
    private Long idAccount;
}
