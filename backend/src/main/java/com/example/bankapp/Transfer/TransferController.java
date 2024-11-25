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
    public ResponseEntity<List<TransferDto>> getTransfersByAccount(@PathVariable Long idAccount, @RequestParam(value = "type", required = false) String type) {

        List<TransferDto> transfers;

        if (type == null) {
            transfers = transferService.getTransfersByAccount(idAccount);
        } else {
            switch (type.toLowerCase()) {
                case "sent" -> transfers = transferService.getTransfersBySender(idAccount);
                case "received" -> transfers = transferService.getTransfersByRecipient(idAccount);
                default -> throw new TransferException("Invalid type parameter. Allowed values: 'sent', 'received'.");
            }
        }

        if (transfers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transfers);
    }


    @PostMapping()
    public ResponseEntity<String> processTransfer(@PathVariable Long idAccount, @RequestBody TransferDto transferDTO) {
        transferService.processTransfer(idAccount, transferDTO);
        return ResponseEntity.ok("Transfer made successfully");
    }

    @GetMapping("/{idTransfer}")
    public ResponseEntity<TransferDto> getTransferDetails(@PathVariable Long idAccount, @PathVariable Long idTransfer) {
        return transferService.getTransferById(idTransfer)
                .filter(transfer -> transfer.getAccount().getIdAccount().equals(idAccount))
                .map(transferMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
