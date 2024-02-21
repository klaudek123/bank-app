package com.example.bankapp.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.idAccount = :idAccount AND l.status = :status")
    boolean existsByAccountIdAndStatus(@Param("idAccount") Long idAccount, @Param("status") String status);
}
