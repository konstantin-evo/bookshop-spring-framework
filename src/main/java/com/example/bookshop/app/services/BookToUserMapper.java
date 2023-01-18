package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookToUserTypeRepository;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.web.dto.BookToUserDto;
import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface BookToUserMapper {

    BookToUserMapper INSTANCE = Mappers.getMapper(BookToUserMapper.class);

    @Mapping(target = "bookToUserId", source = "id")
    @Mapping(target = "bookStatus", source = "type")
    @Mapping(target = "username", source = ".", qualifiedByName = "getUserName")
    @Mapping(target = "bookTitle", source = ".", qualifiedByName = "getBookTitle")
    @Mapping(target = "bookSlug", source = ".", qualifiedByName = "getBookSlug")
    BookToUserDto map(BookToUser bookToUser);

    @Mapping(target = "id", source = "bookToUserId")
    void updateBookToUser(BookToUserDto bookToUserDto, @MappingTarget BookToUser bookToUser, @Context BookToUserTypeRepository bookToUserTypeRepository);

    default String map(BookToUserType bookToUserType) {
        return bookToUserType.getCode().toString();
    }

    @Named("getUserName")
    static String getUserName(BookToUser bookToUser) {
        return bookToUser.getUser().getName();
    }

    @Named("getBookSlug")
    static String getBookSlug(BookToUser bookToUser) {
        return bookToUser.getBook().getSlug();
    }

    @Named("getBookTitle")
    static String getBookTitle(BookToUser bookToUser) {
        return bookToUser.getBook().getTitle();
    }
}
