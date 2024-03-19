package com.example.bankapp.Investment;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/investments")
public class InvestmentController {
    private final InvestmentRepository investmentRepository;
    private final InvestmentService investmentService;

    // Constructor initializing InvestmentController with required repository and service
    public InvestmentController(InvestmentRepository investmentRepository, InvestmentService investmentService) {
        this.investmentRepository = investmentRepository;
        this.investmentService = investmentService;
    }

    // Endpoint to create a new investment
    @PostMapping()
    public ResponseEntity<String> createInvestment(@RequestBody Investment investment){
        return investmentService.createInvestment(investment);
    }

    // Endpoint to retrieve investment details by account ID
    @GetMapping("/{idAccount}")
    public ResponseEntity<List<Investment>> getLoanDetails(@PathVariable Long idAccount){
        List<Investment> investments = investmentService.getInvestmentDetailsByIdAccount(idAccount);
        if (!investments.isEmpty()) {
            return new ResponseEntity<>(investments,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
