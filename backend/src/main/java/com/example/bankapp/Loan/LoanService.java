package com.example.bankapp.Loan;

import com.example.bankapp.Account.AccountService;
import org.springframework.stereotype.Service;


@Service
public class LoanService {
    private final AccountService accountService;
    private final LoanRepository loanRepository;

    public LoanService(AccountService accountService, LoanRepository loanRepository) {
        this.accountService = accountService;
        this.loanRepository = loanRepository;
    }

    public void createLoan(Loan loan) {
        accountService.makeLoan(loan.getIdAccount(), loan.getAmount());
        loanRepository.save(loan);
    }

}
