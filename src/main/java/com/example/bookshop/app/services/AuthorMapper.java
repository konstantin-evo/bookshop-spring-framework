package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.web.dto.AuthorDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    void updateAuthor(AuthorDto bookDto, @MappingTarget Author author);
}
