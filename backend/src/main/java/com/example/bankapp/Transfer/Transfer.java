package com.example.bankapp.Transfer;

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
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransfer;

    @Column(name = "sender", nullable=false)
    private Long sender;

    @Column(name = "recipient", nullable = false)
    private Long recipient;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;


    @PrePersist
    private void prePersist(){
        this.date = LocalDateTime.now();
    }
}
