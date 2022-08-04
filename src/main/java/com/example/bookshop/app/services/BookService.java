package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.*;
import com.example.bookshop.app.model.entity.*;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import com.example.bookshop.app.model.entity.enumuration.TransactionInfo;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookRateDto;
import com.example.bookshop.web.dto.ReviewDto;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;
    private final UserRepository userRepo;
    private final BookToUserTypeRepository typeRepo;
    private final TransactionRepository transactionRepo;

    @Value("${book.coefficient.paid}")
    private double BOOK_COEFFICIENT_PAID;

    @Value("${book.coefficient.viewed}")
    private double BOOK_COEFFICIENT_VIEWED;

    public Page<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findAll(nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfRecentBooks(LocalDate dateFrom, LocalDate dateTo, Integer offset, Integer limit) {
        Date dateFromSql = java.sql.Date.valueOf(dateFrom);
        Date dateToSql = java.sql.Date.valueOf(dateTo);
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("pubDate").descending());
        Page<Book> books = bookRepo.findBookByPubDateIsBetween(dateFromSql, dateToSql, nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("rating").descending());
        Page<Book> books = bookRepo.findAll(nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfViewedBooks(User user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        BookToUserType viewed = typeRepo.findByCode(BookToUserEnum.VIEWED);
        Page<Book> books = bookToUserRepo.findByUserAndType(user, viewed, nextPage).map(BookToUser::getBook);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByTag(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByTag(id, nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getPageOfBooksByGenre(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksGenre(id, nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public Page<BookDto> getBooksByAuthorId(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findBooksByAuthorId(id, nextPage);
        List<BookDto> booksDto = BookMapper.INSTANCE.map(books.getContent());
        return new PageImpl<>(booksDto, nextPage, books.getTotalElements());
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepo.findBooksByAuthorFirstNameContaining(authorName);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        List<Book> books = bookRepo.findBooksByTitleContaining(title);
        if (title.length() <= 1 || books.isEmpty()) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPriceBetween(Integer min, Integer max) {
        List<Book> books = bookRepo.findBooksByPriceBetween(min, max);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getPaidBooks(String userEmail) {
        Integer userId = userRepo.findUserByEmail(userEmail).getId();
        List<Book> books = bookRepo.findPaidBooksByUser(userId);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getArchivedBooks(String userEmail) {
        Integer userId = userRepo.findUserByEmail(userEmail).getId();
        List<Book> books = bookRepo.findArchivedBooksByUser(userId);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithDiscountBetween(Double min, Double max) {
        List<Book> books = bookRepo.findBooksByDiscountBetween(min, max);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithPrice(Integer price) {
        List<Book> books = bookRepo.findBooksByPriceIs(price);
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksWithMaxPrice() {
        List<Book> books = bookRepo.findBooksWithMaxDiscount();
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBestsellers() {
        List<Book> books = bookRepo.findBestsellers();
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getBooksByCookies(String cookie) {
        return BookMapper.INSTANCE.map(findBooksByCookies(cookie));
    }

    public BookDto getBook(String slug) {
        return BookMapper.INSTANCE.map(bookRepo.findBookBySlug(slug));
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
        return BookMapper.INSTANCE.mapBookRateDto(bookRepo.findBookBySlug(slug));
    }

    public ReviewDto getBookReviews(String slug) {
        return BookMapper.INSTANCE.getBookReviews(bookRepo.findBookBySlug(slug));
    }

    public LocalDate convertToLocalDate(String date) {
        return BookMapper.INSTANCE.convertToLocalDate(date);
    }

    /**
     * Method adds books to book purchased by the user and clears the corresponding Cookies
     * if the user has sufficient funds
     *
     * @param cookie   - current value cookie, can contain several books at once
     * @param user     - current session user
     * @param response - the response object is where the servlet can write updated Cookie information
     * @return true if the operation was successful and books are added to the books purchased by the user
     */
    public boolean orderBooks(String cookie, User user, HttpServletResponse response) {

        List<Book> books = findBooksByCookies(cookie);
        int totalPrice = books.stream().mapToInt(Book::getPrice).sum();
        int userBalance = userRepo.getBalance(user.getId());

        if (userBalance >= totalPrice) {
            books.forEach(book -> buyBookByUser(book, user));
            CookieUtil.clearCookieByName(response, "cartContents");
            return true;
        } else {
            return false;
        }
    }

    /**
     * The method checks if the book was previously purchased by the user.\
     * If not, the book is saved as purchased and the user's balance is updated.
     */
    public void buyBookByUser(Book book, User user) {

        BookToUserType paid = typeRepo.findByCode(BookToUserEnum.PAID);
        boolean isBookAlreadyPaid = bookToUserRepo.existsBookToUserByBookAndUserAndType(book, user, paid);

        if (!isBookAlreadyPaid) {
            BookToUser bookToUser = new BookToUser(user, book, paid);
            bookToUserRepo.save(bookToUser);
            bookRepo.updateRating(BOOK_COEFFICIENT_PAID, book.getId());
            userRepo.updateBalance(-book.getPrice(), user.getId());

            // TODO: Fix the TransactionAspect to take this part out of the business logic in BookService
            Transaction transaction = new Transaction(-book.getPrice(), TransactionInfo.BUY_BOOK.getValue(), user, book.getId());
            transactionRepo.save(transaction);
        }
    }

    public void viewBookByUser(User user, String slug) {
        Book book = bookRepo.findBookBySlug(slug);
        BookToUserType viewed = typeRepo.findByCode(BookToUserEnum.VIEWED);
        boolean isBookAlreadyViewed = bookToUserRepo.existsBookToUserByBookAndUserAndType(book, user, viewed);

        if (!isBookAlreadyViewed) {
            BookToUser viewedBook = new BookToUser(user, book, viewed);
            bookToUserRepo.save(viewedBook);
            bookRepo.updateRating(BOOK_COEFFICIENT_VIEWED, book.getId());
        }

    }

    private List<Book> findBooksByCookies(String cookie) {
        if (cookie == null) {
            return Collections.emptyList();
        } else {
            cookie = cookie.startsWith("/") ? cookie.substring(1) : cookie;
            cookie = cookie.endsWith("/") ? cookie.substring(0, cookie.length() - 1) : cookie;
            List<String> cookieSlugs = List.of(cookie.split("/"));
            return bookRepo.findBooksBySlugIn(cookieSlugs);
        }
    }
}
