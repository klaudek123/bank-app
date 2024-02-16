package com.example.bankapp.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findBySenderAndIdAccount(long sender, long idAccount);

    Optional<Transfer> findByRecipientAndIdAccount(long recipient, long idAccount);

    List<Transfer> findByIdAccount(Long idAccount);
}
