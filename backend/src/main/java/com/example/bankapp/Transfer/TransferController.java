package com.example.bankapp.Transfer;

import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;
    private final TransferRepository transferRepository;
    private final AccountService accountService;

    // Constructor initializing TransferController with required services
    public TransferController(TransferService transferService, TransferRepository transferRepository, AccountService accountService) {
        this.transferService = transferService;
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    // Endpoint to retrieve all transfers
    @GetMapping()
    public List<Transfer> getAll() {
        return transferRepository.findAll();
    }

    // Endpoint to initiate a transfer
    @PostMapping()
    public ResponseEntity<String> makeTransfer(@RequestBody Transfer transfer) {
        try {
            transferService.makeTransfer(transfer);
            return ResponseEntity.ok("Transfer made successfully");
        } catch (Exception e) {
            // Handling general server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred");
        }
    }

    // Endpoint to retrieve all transfers by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<List<Transfer>> getAllByIdAccount(@PathVariable Long idAccount) {
        List<Transfer> transfers = transferService.getTransfersByIdAccount(idAccount);
        if (!transfers.isEmpty()) {
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to retrieve all transfers sent from an account
    @GetMapping("/sent")
    public ResponseEntity<Optional<Transfer>> getAllBySender(@RequestParam("sender") Long idAccount) {
        return transferService.getTransfersBySender(idAccount);
    }

    // Endpoint to retrieve all transfers received by an account
    @GetMapping("/received")
    public ResponseEntity<Optional<Transfer>> getAllByRecipient(@RequestParam("recipient") Long idAccount) {
        return transferService.getTransfersByRecipient(idAccount);
    }
}
