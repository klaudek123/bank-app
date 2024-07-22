package com.example.bankapp.Investment;


import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/investments")
public class InvestmentController {
    private final InvestmentService investmentService;
    private final AccountService accountService;

    // Constructor initializing InvestmentController with required repository and service
    public InvestmentController(InvestmentService investmentService, AccountService accountService) {
        this.investmentService = investmentService;
        this.accountService = accountService;
    }

    // Endpoint to create a new investment
    @PostMapping()
    public ResponseEntity<String> createInvestment(@RequestBody InvestmentDto investmentDto){
        if(!accountService.hasSufficientInvestmentBalance(investmentDto.idAccount(), investmentDto.amount())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Brak środków na koncie aby wykonać inwestycje!");
        }
        investmentService.createInvestment(investmentDto);
        return ResponseEntity.ok("Inwestycja została poprawnie rozpoczęta!");
    }

    // Endpoint to retrieve investment details by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<List<InvestmentDto>> getInvestmentDetails(@PathVariable Long idAccount){
        List<InvestmentDto> investments = investmentService.getInvestmentDetailsByIdAccount(idAccount);
        if (!investments.isEmpty()) {
            return new ResponseEntity<>(investments,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
