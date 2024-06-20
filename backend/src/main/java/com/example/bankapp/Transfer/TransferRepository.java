package com.example.bankapp.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Transfer findByIdTransfer(Long idTransfer);

    Optional<Transfer> findBySenderAndAccount_IdAccount(Long sender, Long idAccount);

    Optional<Transfer> findByRecipientAndAccount_IdAccount(Long recipient, Long idAccount);

    List<Transfer> findByAccount_IdAccountOrderByDateDesc(Long idAccount);

}
