package com.example.bankapp.Loan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public LoanController(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;
    }


    @PostMapping()
    public ResponseEntity<String> createLoan(@RequestBody Loan loan){
       if (!loanRepository.existsByAccountIdAndStatus(loan.getIdAccount(), "1")) {
           loanService.createLoan(loan);
           return ResponseEntity.ok("Pożyczka została udzielona!");
       } else {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik posiada już pożyczkę!");
       }
    }



}
