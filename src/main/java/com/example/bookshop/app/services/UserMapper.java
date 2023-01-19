package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.web.dto.UserDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", source = "password")
    User map(RegistrationFormDto registrationForm, String password);

    void updateUser(UserDto userDto, @MappingTarget User user);
}
