package com.example.bankapp.Investment;

import com.example.bankapp.Account.AccountService;
import com.example.bankapp.Mappers.InvestmentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class InvestmentService {
    private final AccountService accountService;
    private final InvestmentRepository investmentRepository;
    private final InvestmentMapper investmentMapper;

    // Constructor initializing InvestmentService with required services and repository
    public InvestmentService(AccountService accountService, InvestmentRepository investmentRepository, InvestmentMapper investmentMapper) {
        this.accountService = accountService;
        this.investmentRepository = investmentRepository;
        this.investmentMapper = investmentMapper;
    }

    // Method to create a new investment.
    public void createInvestment(InvestmentDto investmentDto) {
        //#TODO limits on the number of investments by type

        // Check if the account has sufficient balance for the investment
       Investment investment = investmentMapper.toEntity(investmentDto);

       investmentRepository.save(investment);


       accountService.makeInvestment(investment.getAccount().getIdAccount(), investment.getAmount());

    }

    // Method to retrieve investment details by account ID
    public List<InvestmentDto> getInvestmentDetailsByIdAccount(Long idAccount) {
        return investmentRepository.findByIdAccountAndStatus(idAccount, InvestmentStatus.ACTIVE).stream().map(InvestmentMapper.INSTANCE::toDto).toList();
    }
}
