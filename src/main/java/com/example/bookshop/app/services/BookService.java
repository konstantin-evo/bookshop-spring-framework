package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.dao.BookToUserTypeRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookRateDto;
import com.example.bookshop.web.dto.BookReviewsPageDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;
    private final BookToUserTypeRepository typeRepo;
    private final BookMapper bookMapper;

    public Page<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findActualBooks(nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfRecentBooks(LocalDate dateFrom, LocalDate dateTo, Integer offset, Integer limit) {
        Date dateFromSql = java.sql.Date.valueOf(dateFrom);
        Date dateToSql = java.sql.Date.valueOf(dateTo);
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("pubDate").descending());
        Page<Book> books = bookRepo.findBookByPubDateIsBetween(dateFromSql, dateToSql, nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("popularity").descending());
        Page<Book> books = bookRepo.findActualBooks(nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfViewedBooks(User user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        BookToUserType viewed = typeRepo.findByCode(BookToUserEnum.VIEWED)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUserType.class.getSimpleName(), "BookToUserEnum", BookToUserEnum.VIEWED.name()));
        Page<Book> books = bookToUserRepo.findByUserAndType(user, viewed, nextPage).map(BookToUser::getBook);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByTag(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByTag(id, nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByGenre(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksGenre(id, nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getBooksByAuthorId(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByAuthorId(id, nextPage);
        List<BookDto> booksDto = bookMapper.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepo.findBooksByAuthorFirstNameContaining(authorName);
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        List<Book> books = bookRepo.findBooksByTitleContaining(title);
        if (title.length() <= 1 || books.isEmpty()) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksWithPriceBetween(Integer min, Integer max) {
        List<Book> books = bookRepo.findBooksByPriceBetween(min, max);
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksWithDiscountBetween(Double min, Double max) {
        List<Book> books = bookRepo.findBooksByDiscountBetween(min, max);
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksWithPrice(Integer price) {
        List<Book> books = bookRepo.findBooksByPriceIs(price);
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksWithMaxPrice() {
        List<Book> books = bookRepo.findBooksWithMaxDiscount();
        return bookMapper.map(books);
    }

    public List<BookDto> getBestsellers() {
        List<Book> books = bookRepo.findBestsellers();
        return bookMapper.map(books);
    }

    public List<BookDto> getBooksByCookies(String cookie) {
        List<String> bookSlugs = CookieUtil.getValues(cookie);
        List<Book> books = bookRepo.findBooksBySlugIn(bookSlugs);
        return bookMapper.map(books);
    }

    public BookDto getBook(String slug) {
        return bookMapper.map(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug)));
    }

    public void updateBook(String slug, String path) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));
        book.setImage(path);
        bookRepo.save(book);
    }

    public Pair<String, String> getTotalPricesInCart(String cartContents) {

        List<String> bookSlugs = CookieUtil.getValues(cartContents);
        List<Book> books = bookRepo.findBooksBySlugIn(bookSlugs);

        String priceOld = String.valueOf(books.stream()
                .mapToInt(Book::getPrice).sum());
        String price = String.valueOf(books.stream()
                .mapToInt(book -> (int) (book.getPrice() / (1 - book.getDiscount() / 100))).sum());

        return Pair.of(price, priceOld);
    }

    public BookRateDto getBookRate(String slug) {
        return bookMapper.mapBookRateDto(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug)));
    }

    public BookReviewsPageDto getBookReviews(String slug) {
        return bookMapper.getBookReviews(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug)));
    }

    public LocalDate convertToLocalDate(String date) {
        return bookMapper.convertToLocalDate(date);
    }

}
