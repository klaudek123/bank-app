package com.example.bankapp.Mappers;

import com.example.bankapp.Investment.Investment;
import com.example.bankapp.Investment.InvestmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {
    InvestmentMapper INSTANCE = Mappers.getMapper(InvestmentMapper.class);


    InvestmentDto toDto(Investment investment);


    Investment toEntity(InvestmentDto investmentDto);
}
