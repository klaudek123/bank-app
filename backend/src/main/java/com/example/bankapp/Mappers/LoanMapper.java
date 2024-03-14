package com.example.bankapp.Mappers;

import com.example.bankapp.Loan.Loan;
import com.example.bankapp.Loan.LoanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(source = "idLoan", target = "idLoan")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "idAccount", target = "idAccount")

    LoanDto loanToLoanDto(Loan loan);
}
