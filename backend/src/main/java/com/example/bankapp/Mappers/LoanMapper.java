package com.example.bankapp.Mappers;

import com.example.bankapp.Loan.Loan;
import com.example.bankapp.Loan.LoanDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanDto toDto(Loan loan);

    Loan toEntity(LoanDto loanDto);
}
