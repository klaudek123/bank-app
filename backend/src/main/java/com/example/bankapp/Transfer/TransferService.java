package com.example.bankapp.Transfer;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.TransferMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;
    private final TransferMapper transferMapper;

    // Constructor initializing TransferService with required repositories and services
    public TransferService(TransferRepository transferRepository, AccountService accountService, TransferMapper transferMapper) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
        this.transferMapper = transferMapper;
    }

    // Retrieve all transfers associated with a specific account ID
    public List<Transfer> getTransfersByIdAccount(Long idAccount) {
        return transferRepository.findByAccount_IdAccountOrderByDateDesc(idAccount);
    }

    // Retrieve all transfers sent from a specific account
    public ResponseEntity<Optional<Transfer>> getTransfersBySender(Long idAccount) {
        Optional<Transfer> transfers = transferRepository.findBySenderAndAccount_IdAccount(
                accountService.getNumberByIdAccount(idAccount),
                idAccount);
        if (transfers.isPresent()) {
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve all transfers received by a specific account
    public ResponseEntity<Optional<Transfer>> getTransfersByRecipient(Long idAccount) {
        Optional<Transfer> transfers = transferRepository.findByRecipientAndAccount_IdAccount(
                accountService.getNumberByIdAccount(idAccount),
                idAccount);
        if (transfers.isPresent()) {
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Initiate a transfer between accounts
    public void makeTransfer(TransferDto transferDTO) {
        // Creating a transfer for the sender
        Transfer senderTransfer = new Transfer();
        senderTransfer.setSender(transferDTO.sender());
        senderTransfer.setRecipient(transferDTO.recipient());
        senderTransfer.setAmount(transferDTO.amount());
        senderTransfer.setTitle(transferDTO.title());
        senderTransfer.setAccount(accountService.getAccountById(transferDTO.idAccount()));


        // Creating a transfer for the recipient
        Transfer recipientTransfer = new Transfer();
        recipientTransfer.setSender(transferDTO.sender());
        recipientTransfer.setRecipient(transferDTO.recipient());
        recipientTransfer.setAmount(transferDTO.amount());
        recipientTransfer.setTitle(transferDTO.title());
        recipientTransfer.setAccount(accountService.getAccountById(accountService.getIdAccountByNumber(transferDTO.recipient())));

        // Saving transfers to the repository
        senderTransfer = transferRepository.save(senderTransfer);
        recipientTransfer = transferRepository.save(recipientTransfer);

        // Executing funds transfer between accounts
        accountService.transferFunds(senderTransfer.getAccount().getIdAccount(), recipientTransfer.getAccount().getIdAccount(), senderTransfer.getAmount());
    }
}
