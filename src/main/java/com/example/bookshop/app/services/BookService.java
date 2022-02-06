package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookRateDto;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
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

    public List<BookDto> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        List<Book> books = bookRepo.findBooksByTitleContaining(title);
        if (title.length() <= 1 || books.isEmpty()) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }
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

    public List<BookDto> getBooksByCookies(String cookie) {
        return Mapper.INSTANCE.map(findBooksByCookies(cookie));
    }

    public BookDto getBook(String slug) {
        return Mapper.INSTANCE.map(bookRepo.findBookBySlug(slug));
    }

    public void updateBook(String slug, String path) {
        Book book = bookRepo.findBookBySlug(slug);
        book.setImage(path);
        bookRepo.save(book);
    }

    public Pair<String, String> getTotalPricesInCart(String cartContents) {
        List<Book> books = findBooksByCookies(cartContents);
        String priceOld = String.valueOf(books.stream()
                .mapToInt(Book::getPrice).sum());
        String price = String.valueOf(books.stream()
                .mapToInt(book -> (int) (book.getPrice() / (1 - book.getDiscount() / 100))).sum());
        return Pair.of(price, priceOld);
    }

    public BookRateDto getBookRate(String slug) {
        return Mapper.INSTANCE.mapBookRateDto(bookRepo.findBookBySlug(slug));
    }

    public LocalDate convertToLocalDate(String date) {
        return Mapper.INSTANCE.convertToLocalDate(date);
    }

    /**
     * The method updates a specific Cookie
     * after a user tries to add a Book to this Cookie
     *
     * @param cookieName  the name of the Cookie to update, for example "postponedBooks"
     * @param cookieValue current value cookie, can contain several books at once
     *                    for example "book-bqr-bsi/book-ebf-jyu/book-ekp-gdh"
     * @param slug        unique identifier for the book being added to the Cookie
     * @return updated Cookie
     */
    public Cookie getUpdatedCookies(String cookieValue, String cookieName, String slug) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        if (cookieValue == null || cookieValue.equals("")) {
            cookie.setValue(slug);
        } else if (!cookieValue.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cookieValue).add(slug);
            cookie.setValue(stringJoiner.toString());
        }
        return cookie;
    }

    /**
     * The method removes a book from a specific Cookie
     *
     * @param cookieName  the name of the Cookie to update, for example "postponedBooks"
     * @param cookieValue current value cookie, can contain several books at once
     *                    for example "book-bqr-bsi/book-ebf-jyu/book-ekp-gdh"
     * @param slug        unique identifier for the book being removed from the Cookie
     */
    public Cookie removeBookFromCookie(String cookieValue, String cookieName, String slug) {
        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieValue.split("/")));
        cookieBooks.remove(slug);
        Cookie cookie = new Cookie(cookieName, String.join("/", cookieBooks));
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Util method that allows to determine if the current Cookie value is empty or not
     * Used to define attributes such as "isCartEmpty", etc.
     */
    public boolean getBooleanAttribute(String cookieValue) {
        if (cookieValue == null || cookieValue.equals("")) {
            return true;
        } else {
            return (findBooksByCookies(cookieValue).isEmpty());
        }
    }

    private List<Book> findBooksByCookies(String cookie) {
        if (cookie == null) {
            return Collections.emptyList();
        } else {
            cookie = cookie.startsWith("/") ? cookie.substring(1) : cookie;
            cookie = cookie.endsWith("/") ? cookie.substring(0, cookie.length() - 1) : cookie;
            String[] cookieSlugs = cookie.split("/");
            return bookRepo.findBooksBySlugIn(cookieSlugs);
        }
    }
}
