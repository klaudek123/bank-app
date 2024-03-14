package com.example.bankapp.Loan;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.LoanMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoanService {
    private final AccountService accountService;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    public LoanService(AccountService accountService, LoanRepository loanRepository, LoanMapper loanMapper) {
        this.accountService = accountService;
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    public void createLoan(Loan loan) {
        accountService.makeLoan(loan.getIdAccount(), loan.getAmount());
        loanRepository.save(loan);
    }

    public Optional<LoanDto> getLoanDetailsByIdAccount(Long idAccount) {
        Optional<Loan> loan =  loanRepository.findByIdAccountAndStatus(idAccount, "1");


        return loan.map(loanMapper::loanToLoanDto);
    }
}
