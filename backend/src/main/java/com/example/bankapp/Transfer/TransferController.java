package com.example.bankapp.Transfer;

import com.example.bankapp.Mappers.TransferMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts/{idAccount}/transfers")
public class TransferController {
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    public TransferController(TransferService transferService, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.transferMapper = transferMapper;
    }

    @GetMapping()
    public ResponseEntity<List<TransferDto>> getTransfersByAccount(@PathVariable Long idAccount,
                                                                   @RequestParam(value = "type", required = false) String type) {
        List<TransferDto> transfers;

        if ("sent".equalsIgnoreCase(type)) {
            transfers = transferService.getTransfersBySender(idAccount);
        } else if ("received".equalsIgnoreCase(type)) {
            transfers = transferService.getTransfersByRecipient(idAccount);
        } else {
            transfers = transferService.getTransfersByAccount(idAccount);
        }

        if (!transfers.isEmpty()) {
            return ResponseEntity.ok(transfers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{idTransfer}")
    public ResponseEntity<TransferDto> getTransferDetails(@PathVariable Long idAccount, @PathVariable Long idTransfer) {
        return transferService.getTransferById(idTransfer)
                .filter(transfer -> transfer.getAccount().getIdAccount().equals(idAccount))
                .map(transferMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<String> makeTransfer(@PathVariable Long idAccount, @RequestBody TransferDto transferDTO) {
        if (transferDTO.recipient().equals(transferDTO.sender())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sender and recipient cannot be the same");
        } else if (transferDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transfer amount must be greater than zero");
        } else {
            transferService.makeTransfer(idAccount, transferDTO);
            return ResponseEntity.ok("Transfer made successfully");
        }

    }


}
