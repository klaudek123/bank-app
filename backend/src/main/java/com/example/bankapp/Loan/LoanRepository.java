package com.example.bankapp.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.account.idAccount = :idAccount AND l.status = :status")
    boolean existsByAccount_IdAccountAndStatus(@Param("idAccount") Long idAccount, @Param("status") String status);

    Optional<Loan> findByAccount_IdAccountAndStatus(Long idAccount, String status);
}
