package com.example.bankapp.Investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT i FROM Investment i WHERE i.account.idAccount = :idAccount AND i.status = :status")
    List<Investment> findByIdAccountAndStatus(Long idAccount, InvestmentStatus status);
}
