package com.example.bankapp.Mappers;

import com.example.bankapp.Transfer.Transfer;
import com.example.bankapp.Transfer.TransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    @Mapping(source = "account.idAccount", target = "idAccount")
    TransferDto toDto(Transfer transfer);

    @Mapping(source = "idAccount", target = "account.idAccount")
    Transfer toEntity(TransferDto transferDto);

}
