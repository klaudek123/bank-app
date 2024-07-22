package com.example.bankapp.Mappers;

import com.example.bankapp.User.User;
import com.example.bankapp.User.UserDto;
import com.example.bankapp.User.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


import java.time.LocalDate;
import java.sql.Date;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "localDateToDate")
    User userRegisterDTOtoUser(UserRegisterDto userRegisterDTO);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "dateToLocalDate")
    UserDto userToUserDTO(User user);

    @Named("localDateToDate")
    static Date localDateToDate(LocalDate localDate) {
        return (localDate == null) ? null : Date.valueOf(localDate);
    }

    @Named("dateToLocalDate")
    static LocalDate dateToLocalDate(Date date) {
        return (date == null) ? null : date.toLocalDate();
    }
}







