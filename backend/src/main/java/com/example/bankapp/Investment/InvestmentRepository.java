package com.example.bankapp.Investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

//    @Query("SELECT i FROM Investment i WHERE i.idAccount = :idAccount AND i.status = :status")
    Optional<Investment> findByIdAccountAndStatus(Long idAccount, InvestmentStatus status);
}
