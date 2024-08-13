package com.example.bankapp.Investment;

import com.example.bankapp.Account.Account;
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

    public InvestmentService(AccountService accountService, InvestmentRepository investmentRepository, InvestmentMapper investmentMapper) {
        this.accountService = accountService;
        this.investmentRepository = investmentRepository;
        this.investmentMapper = investmentMapper;
    }

    public void createInvestment(Long idAccount, InvestmentDto investmentDto) {
        //#TODO limits on the number of investments by type

        Investment investment = investmentMapper.toEntity(investmentDto);

        Account account = accountService.getAccountById(idAccount);
        investment.setAccount(account);
        investment.setStatus(InvestmentStatus.ACTIVE);
        investment.setName("default");

        investmentRepository.save(investment);


        accountService.makeInvestment(investment.getAccount().getIdAccount(), investment.getAmount());

    }

    public List<InvestmentDto> getInvestmentDetailsByIdAccount(Long idAccount) {
        return investmentRepository.findByIdAccountAndStatus(idAccount, InvestmentStatus.ACTIVE)
                .stream()
                .map(InvestmentMapper.INSTANCE::toDto)
                .toList();
    }
}
