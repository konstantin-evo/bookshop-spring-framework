package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.AuthorDao;
import com.example.bookshop.app.model.dao.BookDao;
import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookDao bookRepo;
    private final AuthorDao authorRepo;

    @Autowired
    public BookService(BookDao bookRepo, AuthorDao authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public List<BookDto> getBooksData() {
        return bookRepo.getAll().stream().map(book -> {
            BookDto bookDto = Mapper.INSTANCE.bookToDto(book);
            Author author = authorRepo.getAuthorByBookId(book.getId());
            AuthorDto authorDto = Mapper.INSTANCE.authorToDto(author);
            bookDto.setAuthor(authorDto);
            return bookDto;
        }).collect(Collectors.toList());
    }
}
