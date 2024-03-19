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

    // Constructor initializing LoanService with required repositories, services, and mapper
    public LoanService(AccountService accountService, LoanRepository loanRepository, LoanMapper loanMapper) {
        this.accountService = accountService;
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    // Method to create a new loan
    public void createLoan(Loan loan) {
        // Make the loan and save it in the repository.
        accountService.makeLoan(loan.getIdAccount(), loan.getAmount());
        loanRepository.save(loan);
    }

    // Method to retrieve loan details by account ID
    public Optional<LoanDto> getLoanDetailsByIdAccount(Long idAccount) {
        // Retrieve the loan by account ID and status
        Optional<Loan> loan = loanRepository.findByIdAccountAndStatus(idAccount, "1");

        // Map the loan entity to a DTO if present
        return loan.map(loanMapper::loanToLoanDto);
    }
}
