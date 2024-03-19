package com.example.bankapp.Transfer;

import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;

    // Constructor initializing TransferService with required repositories and services
    public TransferService(TransferRepository transferRepository, AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    // Retrieve all transfers associated with a specific account ID
    public List<Transfer> getTransfersByIdAccount(Long idAccount) {
        return transferRepository.findByIdAccountOrderByDateDesc(idAccount);
    }

    // Retrieve all transfers sent from a specific account
    public ResponseEntity<Optional<Transfer>> getTransfersBySender(Long idAccount) {
        Optional<Transfer> transfers = transferRepository.findBySenderAndIdAccount(
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
        Optional<Transfer> transfers = transferRepository.findByRecipientAndIdAccount(
                accountService.getNumberByIdAccount(idAccount),
                idAccount);
        if (transfers.isPresent()) {
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Initiate a transfer between accounts
    public void makeTransfer(Transfer transfer) {
        Transfer senderTransfer = new Transfer();
        senderTransfer.setSender(accountService.getNumberByIdAccount(transfer.getIdAccount()));
        senderTransfer.setRecipient(transfer.getRecipient());
        senderTransfer.setAmount(transfer.getAmount());
        senderTransfer.setTitle(transfer.getTitle());
        senderTransfer.setIdAccount(transfer.getIdAccount());

        Transfer recipientTransfer = new Transfer();
        recipientTransfer.setSender(accountService.getNumberByIdAccount(transfer.getIdAccount()));
        recipientTransfer.setRecipient(transfer.getRecipient());
        recipientTransfer.setAmount(transfer.getAmount());
        recipientTransfer.setTitle(transfer.getTitle());
        recipientTransfer.setIdAccount(accountService.getIdAccountByNumber(transfer.getRecipient()));

        // Save transfers in repository and perform funds transfer between accounts
        transferRepository.save(senderTransfer);
        transferRepository.save(recipientTransfer);
        accountService.transferFunds(senderTransfer.getIdAccount(), recipientTransfer.getIdAccount(), senderTransfer.getAmount());
    }
}
