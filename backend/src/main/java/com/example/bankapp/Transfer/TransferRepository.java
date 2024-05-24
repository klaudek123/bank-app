package com.example.bankapp.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Transfer findByIdTransfer(Long idTransfer);

    Optional<Transfer> findBySenderAndIdAccount(Long sender, Long idAccount);

    Optional<Transfer> findByRecipientAndIdAccount(Long recipient, Long idAccount);

    List<Transfer> findByIdAccount(Long idAccount);

    List<Transfer> findByIdAccountOrderByDateDesc(Long idAccount);

}
