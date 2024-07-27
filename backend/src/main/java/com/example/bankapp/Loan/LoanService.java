package com.example.bankapp.Loan;

import com.example.bankapp.Account.Account;
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
    public void createLoan(LoanDto loanDto) {
        // Make the loan and save it in the repository.
        accountService.makeLoan(loanDto.getIdAccount(), loanDto.getAmount());


        // Get Account by idAccount from loanDto
        Account account = accountService.getAccountById(loanDto.getIdAccount());

        Loan loan = loanMapper.toEntity(loanDto);
        loan.setAccount(account);
        loanRepository.save(loan);
    }

    // Method to retrieve loan details by account ID
    public Optional<LoanDto> getLoanDetailsByIdAccount(Long idAccount) {
        // Retrieve the loan by account ID and status
        Optional<Loan> loan = loanRepository.findByAccount_IdAccountAndStatus(idAccount, "1");

        // Map the loan entity to a DTO if present
        return loan.map(loanMapper::toDto);
    }
}
