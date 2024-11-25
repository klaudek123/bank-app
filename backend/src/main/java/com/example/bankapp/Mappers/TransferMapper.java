package com.example.bankapp.Mappers;

import com.example.bankapp.Transfer.Transfer;
import com.example.bankapp.Transfer.TransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    TransferDto toDto(Transfer transfer);

    Transfer toEntity(TransferDto transferDto);

}
