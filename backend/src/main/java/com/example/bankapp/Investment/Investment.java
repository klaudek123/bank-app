package com.example.bankapp.Investment;

import com.example.bankapp.Account.Account;
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

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "start_date",columnDefinition = "DATESTAMP")
    private String startDate;

    @Column(name = "end_date", columnDefinition = "DATESTAMP")
    private String endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account", nullable = false)
    private Account account;
}