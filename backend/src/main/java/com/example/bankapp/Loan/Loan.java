package com.example.bankapp.Loan;

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
    private String name;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status; // 1 - active, 0 - inactive
    private Long idAccount;


    @PrePersist
    public void prePresist(){
        this.name = "loan" + this.idLoan;

        if(this.endDate.isAfter(this.startDate) && LocalDateTime.now().isBefore(endDate)){
            this.status = "1";
        }
        else{
            this.status = "0";
        }
    }

}
