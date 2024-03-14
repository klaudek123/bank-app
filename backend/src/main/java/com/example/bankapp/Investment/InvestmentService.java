package com.example.bankapp.Investment;


import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvestmentService {
    private final AccountService accountService;
    private final InvestmentRepository investmentRepository;

    public InvestmentService(AccountService accountService, InvestmentRepository investmentRepository) {
        this.accountService = accountService;
        this.investmentRepository = investmentRepository;
    }


    public ResponseEntity<String> createInvestment(Investment investmentDto) {
        //#TODO limits on the number of investments by type

        if(accountService.hasSufficientInvestmentBalance(investmentDto.getIdAccount(), investmentDto.getAmount())){
            Investment investment = new Investment();
            investment.setName("inv:"+investmentDto.getIdAccount()+":"+investmentDto.getAmount()+":"+investmentDto.getInterestRate()+":"+investmentDto.getType());
            investment.setType(investmentDto.getType());
            investment.setAmount(investmentDto.getAmount());
            investment.setInterestRate(investmentDto.getInterestRate());
//            investment.setStartDate(LocalDateTime.parse(investmentDto.getStartDate().toString())); // Sparsowanie daty
//            investment.setEndDate(LocalDateTime.parse(investmentDto.getEndDate().toString())); // Sparsowanie daty
            investment.setStartDate(String.valueOf(investmentDto.getStartDate())); // Sparsowanie daty
            investment.setEndDate(String.valueOf(investmentDto.getEndDate())); // Sparsowanie daty
            investment.setStatus(InvestmentStatus.ACTIVE);
            investment.setIdAccount(Long.parseLong(String.valueOf(investmentDto.getIdAccount())));
            investmentRepository.save(investment);
            accountService.makeInvestment(investment.getIdAccount(), investment.getAmount());
            return ResponseEntity.ok("Inwestycja została poprawnie rozpoczęta!");
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Brak środków na koncie aby wykonać inwestycje!");
        }

    }

    public Optional<Investment> getInvestmentDetailsByIdAccount(Long idAccount) {
        return investmentRepository.findByIdAccountAndStatus(idAccount, InvestmentStatus.ACTIVE);
    }
}
