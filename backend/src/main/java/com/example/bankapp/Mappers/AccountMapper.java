package com.example.bankapp.Mappers;

import com.example.bankapp.Account.Account;
import com.example.bankapp.Account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "number", target = "number")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "dateOfCreation", target = "dateOfCreation")
    @Mapping(source = "status", target = "status")
    AccountDto accountToAccountDto(Account account);
}