package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.web.dto.AuthorDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    void updateAuthor(AuthorDto bookDto, @MappingTarget Author author);
}
