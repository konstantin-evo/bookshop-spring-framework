package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<BookDto> getBooksData() {
        return bookRepo.findAll().stream().map(book -> {
            BookDto bookDto = Mapper.INSTANCE.bookToDto(book);
            AuthorDto authorDto = Mapper.INSTANCE.authorToDto(book.getAuthor());
            bookDto.setAuthor(authorDto);
            return bookDto;
        }).collect(Collectors.toList());
    }
}
