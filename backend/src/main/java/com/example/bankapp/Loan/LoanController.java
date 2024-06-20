package com.example.bankapp.Loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    // Constructor initializing LoanController with required services and repository
    public LoanController(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;

    }

    // Endpoint to create a new loan
    @PostMapping()
    public ResponseEntity<String> createLoan(@RequestBody LoanDto loan) {
        if (!loanRepository.existsByAccount_IdAccountAndStatus(loan.getIdAccount(), "1")) {
            loanService.createLoan(loan);
            return ResponseEntity.ok("Loan has been granted!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has an active loan!");
        }
    }

    // Endpoint to retrieve loan details by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<Optional<LoanDto>> getLoanDetails(@PathVariable Long idAccount) {
        Optional<LoanDto> loan = loanService.getLoanDetailsByIdAccount(idAccount);

        if (loan.isPresent()) {
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
