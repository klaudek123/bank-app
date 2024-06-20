package com.example.bankapp.Mappers;

import com.example.bankapp.Investment.Investment;
import com.example.bankapp.Investment.InvestmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {
    InvestmentMapper INSTANCE = Mappers.getMapper(InvestmentMapper.class);

    @Mapping(source = "account.idAccount", target = "idAccount")
    InvestmentDto toDto(Investment investment);

    @Mapping(source = "idAccount", target = "account.idAccount")
    Investment toEntity(InvestmentDto investmentDto);
}
