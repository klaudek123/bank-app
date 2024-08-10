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

    public LoanService(AccountService accountService, LoanRepository loanRepository, LoanMapper loanMapper) {
        this.accountService = accountService;
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    public void createLoan(Long idAccount, LoanDto loanDto) {
        accountService.grantLoan(idAccount, loanDto.amount());

        Account account = accountService.getAccountById(idAccount);

        Loan loan = loanMapper.toEntity(loanDto);
        loan.setAccount(account);
        loanRepository.save(loan);
    }

    public Optional<LoanDto> getLoanDetailsByIdAccount(Long idAccount) {
        return loanRepository.findByAccount_IdAccountAndStatus(idAccount, LoanStatus.ACTIVE).map(loanMapper::toDto);
    }
}
