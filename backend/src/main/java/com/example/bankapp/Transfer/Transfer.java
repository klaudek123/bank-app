package com.example.bankapp.Transfer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTransfer;
    private long sender;
    private long recipient;
    private String title;
    @Column(columnDefinition = "DATESTAMP")
    private String date;
    private BigDecimal amount;
    private long idAccount;


    @PrePersist
    private void prePresist(){
        this.date = String.valueOf(LocalDateTime.now());
    }
}
