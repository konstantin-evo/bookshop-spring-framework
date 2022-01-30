package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<BookDto> getBooksData() {
        return Mapper.INSTANCE.map(bookRepo.findAll());
    }

    public Page<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findAll(nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfRecentBooks(LocalDate dateFrom, LocalDate dateTo, Integer offset, Integer limit) {
        Date dateFromSql = java.sql.Date.valueOf(dateFrom);
        Date dateToSql = java.sql.Date.valueOf(dateTo);
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("pubDate").descending());
        Page<Book> books = bookRepo.findBookByPubDateIsBetween(dateFromSql, dateToSql, nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBookByTitleContaining(searchWord, nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("rating").descending());
        Page<Book> books = bookRepo.findAll(nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByTag(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByTag(id, nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByGenre(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksGenre(id, nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getBooksByAuthorId(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByAuthorId(id, nextPage);
        List<BookDto> booksDto = Mapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepo.findBooksByAuthorFirstNameContaining(authorName);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksByTitle(String title) {
        List<Book> books = bookRepo.findBooksByTitleContaining(title);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPriceBetween(Integer min, Integer max) {
        List<Book> books = bookRepo.findBooksByPriceBetween(min, max);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithDiscountBetween(Double min, Double max) {
        List<Book> books = bookRepo.findBooksByDiscountBetween(min, max);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPrice(Integer price) {
        List<Book> books = bookRepo.findBooksByPriceIs(price);
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithMaxPrice() {
        List<Book> books = bookRepo.findBooksWithMaxDiscount();
        return Mapper.INSTANCE.map(books);
    }

    public List<BookDto> getBestsellers() {
        List<Book> books = bookRepo.findBestsellers();
        return Mapper.INSTANCE.map(books);
    }

    public LocalDate convertToLocalDate(String date) {
        return Mapper.INSTANCE.convertToLocalDate(date);
    }

}
