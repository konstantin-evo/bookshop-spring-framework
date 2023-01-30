package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.web.dto.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface UserMapper {

    @Mapping(target = "password", source = "password")
    User map(RegistrationFormDto registrationForm, String password);

    void updateUser(UserDto userDto, @MappingTarget User user);
}
