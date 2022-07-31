package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.ProfileChanges;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.ProfileDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "profileDto.name")
    @Mapping(target = "phone", source = "profileDto.phone")
    @Mapping(target = "password", source = "encryptedPassword")
    @Mapping(target = "user.id", source = "user.id")
    ProfileChanges map(ProfileDto profileDto, User user, String encryptedPassword);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget User user, ProfileChanges profileChanges);
}
