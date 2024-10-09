package com.example.bankapp.Account;

import com.example.bankapp.Investment.Investment;
import com.example.bankapp.Loan.Loan;
import com.example.bankapp.Transfer.Transfer;
import com.example.bankapp.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    private Long idAccount;

    @Column(name = "number", nullable = false, unique = true)
    private Long number;

    @Column(name = "password", nullable = false, length = 72)
    private String password;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "date_of_creation", nullable = false, columnDefinition = "DATE")
    private LocalDateTime dateOfCreation;

    @Column(name = "type", length = 15, nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Investment> investments;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transfer> transfers = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dateOfCreation = LocalDateTime.now();
    }


}