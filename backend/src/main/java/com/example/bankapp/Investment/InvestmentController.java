package com.example.bankapp.Investment;


import com.example.bankapp.Account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts/{idAccount}/investments")
public class InvestmentController {
    private final InvestmentService investmentService;
    private final AccountService accountService;

    public InvestmentController(InvestmentService investmentService, AccountService accountService) {
        this.investmentService = investmentService;
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<String> createInvestment(@PathVariable Long idAccount, @RequestBody InvestmentDto investmentDto){
        System.out.println(investmentDto.endDate());
        if(!accountService.hasSufficientBalance(idAccount, investmentDto.amount())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There are no funds in the account to make the investment.");
        }
        investmentService.createInvestment(idAccount, investmentDto);
        return ResponseEntity.ok("The investment has been started successfully.");
    }

    @GetMapping()
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
