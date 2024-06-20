package com.example.bankapp.Account;

import com.example.bankapp.Loan.Loan;
import com.example.bankapp.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "date_of_creation", nullable = false, columnDefinition = "DATE")
    private LocalDateTime dateOfCreation;

    @Column(name = "type", length = 15)
    private String type;

    @Column(name = "status", length = 1)
    private String status; // 1 = active, 0 = inactive

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dateOfCreation = LocalDateTime.now();
    }


}