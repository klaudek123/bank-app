package com.example.bankapp.Investment;

import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class InvestmentService {
    private final AccountService accountService;
    private final InvestmentRepository investmentRepository;

    // Constructor initializing InvestmentService with required services and repository
    public InvestmentService(AccountService accountService, InvestmentRepository investmentRepository) {
        this.accountService = accountService;
        this.investmentRepository = investmentRepository;
    }

    // Method to create a new investment.
    public ResponseEntity<String> createInvestment(Investment investmentDto) {
        //#TODO limits on the number of investments by type

        // Check if the account has sufficient balance for the investment
        if (accountService.hasSufficientInvestmentBalance(investmentDto.getIdAccount(), investmentDto.getAmount())) {
            Investment investment = new Investment();
            investment.setName("inv:" + investmentDto.getIdAccount() + ":" + investmentDto.getAmount() + ":" + investmentDto.getInterestRate() + ":" + investmentDto.getType());
            investment.setType(investmentDto.getType());
            investment.setAmount(investmentDto.getAmount());
            investment.setInterestRate(investmentDto.getInterestRate());

            // Parsing the start date
            LocalDateTime startDate = LocalDateTime.parse(investmentDto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            investment.setStartDate(String.valueOf(startDate));

            // Parsing the end date
            LocalDateTime endDate = LocalDateTime.parse(investmentDto.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            investment.setEndDate(String.valueOf(endDate));

            investment.setStatus(InvestmentStatus.ACTIVE);
            investment.setIdAccount(Long.parseLong(String.valueOf(investmentDto.getIdAccount())));
            investmentRepository.save(investment);
            accountService.makeInvestment(investment.getIdAccount(), investment.getAmount());
            return ResponseEntity.ok("Inwestycja została poprawnie rozpoczęta!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Brak środków na koncie aby wykonać inwestycje!");
        }

    }

    // Method to retrieve investment details by account ID
    public List<Investment> getInvestmentDetailsByIdAccount(Long idAccount) {
        return investmentRepository.findByIdAccountAndStatus(idAccount, InvestmentStatus.ACTIVE);
    }
}
