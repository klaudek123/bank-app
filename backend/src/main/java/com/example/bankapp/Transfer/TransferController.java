package com.example.bankapp.Transfer;

import com.example.bankapp.Mappers.TransferMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    // Constructor initializing TransferController with required services
    public TransferController(TransferService transferService, TransferRepository transferRepository, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
    }

    // Endpoint to retrieve all transfers
    @GetMapping()
    public List<TransferDto> getAll() {

        return transferRepository.findAll().stream()
                .map(transferMapper::toDto)
                .collect(Collectors.toList());
    }


    @GetMapping("/transfer/{idTransfer}")
    public ResponseEntity<TransferDto> getTransferDetails(@PathVariable Long idTransfer) {
        return transferRepository.findById(idTransfer)
                .map(transferMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to initiate a transfer
    @PostMapping()
    public ResponseEntity<String> makeTransfer(@RequestBody TransferDto transferDTO) {
        if (transferDTO.recipient().equals(transferDTO.sender())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sender and recipient cannot be the same");
        } else if (transferDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transfer amount must be greater than zero");
        } else {
            try {
                transferService.makeTransfer(transferDTO);
                return ResponseEntity.ok("Transfer made successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred");
            }
        }

    }

    // Endpoint to retrieve all transfers by account ID
    @GetMapping("/account/{idAccount}")
    public ResponseEntity<List<TransferDto>> getAllByIdAccount(@PathVariable Long idAccount) {
        List<TransferDto> transfers = transferService.getTransfersByIdAccount(idAccount).stream()
                .map(transferMapper::toDto)
                .collect(Collectors.toList());
        if (!transfers.isEmpty()) {
            return ResponseEntity.ok(transfers);
        } else {
            return ResponseEntity.notFound().build();
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
