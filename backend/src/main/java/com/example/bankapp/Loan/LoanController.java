package com.example.bankapp.Loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accounts/{idAccount}/loans")
public class LoanController {
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public LoanController(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;

    }

    @PostMapping()
    public ResponseEntity<String> createLoan(@PathVariable Long idAccount, @RequestBody LoanDto loan) {
        if (!loanRepository.existsByAccount_IdAccountAndStatus(idAccount, LoanStatus.ACTIVE)) {
            loanService.createLoan(idAccount, loan);
            return ResponseEntity.ok("Loan has been granted!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has an active loan!");
        }
    }

    @GetMapping()
    public ResponseEntity<LoanDto> getLoanDetails(@PathVariable Long idAccount) {
        return loanService.getLoanDetailsByIdAccount(idAccount)
                .map(loan -> new ResponseEntity<>(loan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
