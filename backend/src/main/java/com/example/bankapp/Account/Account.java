package com.example.bankapp.Account;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccount;

    private Long number;
    private String password;
    private BigDecimal balance;
    @Column(columnDefinition = "DATE")
    private LocalDateTime dateOfCreation;
    private String status; // 1 = active, 0 = unactive
    private Long idUser;

    @PrePersist
    public void prePersist(){
        this.dateOfCreation = LocalDateTime.now();
    }


}