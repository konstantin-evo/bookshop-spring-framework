package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface  Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    BookDto bookToDto(Book book);
    AuthorDto authorToDto(Author author);

}
