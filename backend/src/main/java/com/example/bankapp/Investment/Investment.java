package com.example.bankapp.Investment;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "investment")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInvestment;
    private String name;
    @Enumerated(EnumType.STRING)
    private InvestmentType type;
    private BigDecimal amount;
    private BigDecimal interestRate;
    @Column(columnDefinition = "DATESTAMP")
    private String startDate;
    @Column(columnDefinition = "DATESTAMP")
    private String endDate;
    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;
    private Long idAccount;
}