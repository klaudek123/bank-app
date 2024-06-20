package com.example.bankapp.Loan;

import com.example.bankapp.Account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoan;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false)
    private String status; // 1 - active, 0 - inactive


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account", nullable = false)
    private Account account;

    @PrePersist
    public void prePresist() {
//        this.name = "loan" + this.idLoan;

        if (this.endDate.isAfter(this.startDate) && LocalDateTime.now().isBefore(endDate)) {
            this.status = "1";
        } else {
            this.status = "0";
        }
    }

}
