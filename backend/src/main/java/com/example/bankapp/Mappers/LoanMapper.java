package com.example.bankapp.Mappers;

import com.example.bankapp.Loan.Loan;
import com.example.bankapp.Loan.LoanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(source = "idLoan", target = "idLoan")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "account.idAccount", target = "idAccount")
    LoanDto toDto(Loan loan);

    @Mapping(source = "idLoan", target = "idLoan")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "idAccount", target = "account.idAccount")
    Loan toEntity(LoanDto loanDto);
}
