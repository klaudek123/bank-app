package com.example.bankapp.Transfer;

import com.example.bankapp.Account.AccountDto;
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


    public TransferController(TransferService transferService, TransferRepository transferRepository, AccountService accountService) {
        this.transferService = transferService;
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    @GetMapping()
    public List<Transfer> getAll(){
        return transferRepository.findAll();
    }


    @PostMapping()
    public ResponseEntity<String> makeTransfer(@RequestBody Transfer transfer){
        try{
            transferService.makeTransfer(transfer);
            return ResponseEntity.ok("Transfer made successfully");
        } catch (Exception e){
            // W przypadku ogólnego błędu serwera
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred");
        }
    }

    @GetMapping("/{idAccount}")
    public ResponseEntity<List<Transfer>> getAllByIdAccount(@PathVariable Long idAccount){
        List<Transfer> transfers =  transferService.getTransfersByIdAccount(idAccount);
        if(!transfers.isEmpty()){
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<Optional<Transfer>> getAllBySender(@RequestParam("sender") Long idAccount){
        return transferService.getTransfersBySender(idAccount);
    }

    @GetMapping("/received")
    public ResponseEntity<Optional<Transfer>> getAllByRecipient(@RequestParam("recipient") Long idAccount){
        return transferService.getTransfersByRecipient(idAccount);
    }



}
