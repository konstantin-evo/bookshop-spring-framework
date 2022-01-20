package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<BookDto> getBooksData() {
        return Mapper.INSTANCE.map(bookRepo.findAll());
    }

    public Page<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        Page<Book> books = bookRepo.findAll(nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public List<BookDto> getBooksByAuthor(String authorName){
        List<Book> books = bookRepo.findBooksByAuthorFirstNameContaining(authorName);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksByTitle(String title){
        List<Book> books = bookRepo.findBooksByTitleContaining(title);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPriceBetween(Integer min, Integer max){
        List<Book> books = bookRepo.findBooksByPriceBetween(min, max);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithDiscountBetween(Double min, Double max){
        List<Book> books = bookRepo.findBooksByDiscountBetween(min, max);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPrice(Integer price){
        List<Book> books = bookRepo.findBooksByPriceIs(price);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithMaxPrice(){
        List<Book> books =  bookRepo.getBooksWithMaxDiscount();
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBestsellers(){
        List<Book> books = bookRepo.getBestsellers();
        return Mapper.INSTANCE.map(books);
    }
}
