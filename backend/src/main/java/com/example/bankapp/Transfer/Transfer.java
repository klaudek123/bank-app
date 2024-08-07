package com.example.bankapp.Transfer;

import com.example.bankapp.Account.Account;
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
    private Long idTransfer;
    private Long sender;
    private Long recipient;
    private String title;
    @Column(columnDefinition = "DATESTAMP")
    private String date;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    private Account account;


    @PrePersist
    private void prePersist(){
        this.date = String.valueOf(LocalDateTime.now());
    }
}
