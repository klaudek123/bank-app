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

    public Transfer getTransferDetails(Long idTransfer) {
        return transferRepository.findByIdTransfer(idTransfer);
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
        // Creating a transfer for the sender
        Transfer senderTransfer = new Transfer();
        senderTransfer.setSender(transfer.getSender());
        senderTransfer.setRecipient(transfer.getRecipient());
        senderTransfer.setAmount(transfer.getAmount());
        senderTransfer.setTitle(transfer.getTitle());
        senderTransfer.setAccount(transfer.getAccount());

        // Creating a transfer for the recipient
        Transfer recipientTransfer = new Transfer();
        recipientTransfer.setSender(transfer.getSender());
        recipientTransfer.setRecipient(transfer.getRecipient());
        recipientTransfer.setAmount(transfer.getAmount());
        recipientTransfer.setTitle(transfer.getTitle());
        recipientTransfer.setAccount(transfer.getAccount());

        // Saving transfers to the repository
        senderTransfer = transferRepository.save(senderTransfer);
        recipientTransfer = transferRepository.save(recipientTransfer);

        // Executing funds transfer between accounts
        accountService.transferFunds(senderTransfer.getAccount().getIdAccount(), recipientTransfer.getAccount().getIdAccount(), senderTransfer.getAmount());
    }
}
