package com.example.bankapp.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findBySenderAndIdAccount(Long sender, Long idAccount);

    Optional<Transfer> findByRecipientAndIdAccount(Long recipient, Long idAccount);

    List<Transfer> findByIdAccount(Long idAccount);

    List<Transfer> findByIdAccountOrderByDateDesc(Long idAccount);
}
