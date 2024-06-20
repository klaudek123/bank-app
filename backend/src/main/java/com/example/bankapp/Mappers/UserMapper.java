package com.example.bankapp.Mappers;

import com.example.bankapp.User.User;
import com.example.bankapp.User.UserDto;
import com.example.bankapp.User.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    User userRegisterDTOtoUser(UserRegisterDto userRegisterDTO);


    UserDto userToUserDTO(User user);
}
