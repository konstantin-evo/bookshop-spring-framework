package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.dao.BookToUserTypeRepository;
import com.example.bookshop.app.model.dao.TransactionRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.Transaction;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import com.example.bookshop.app.model.entity.enumuration.TransactionInfo;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookRateDto;
import com.example.bookshop.web.dto.ReviewDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import com.example.bookshop.web.exception.BookstoreApiWrongParameterException;
import com.example.bookshop.web.exception.UserNotFoundException;
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

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;
    private final UserRepository userRepo;
    private final BookToUserTypeRepository typeRepo;
    private final TransactionRepository transactionRepo;

    public Page<BookDto> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<Book> books = bookRepo.findActualBooks(nextPage);
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
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("popularity").descending());
        Page<Book> books = bookRepo.findActualBooks(nextPage);
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
        User user = userRepo.findUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail, true));
        List<Book> books = bookRepo.findPaidBooksByUser(user.getId());
        return BookMapper.INSTANCE.map(books);
    }

    public List<BookDto> getArchivedBooks(String userEmail) {
        User user = userRepo.findUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail, true));
        List<Book> books = bookRepo.findArchivedBooksByUser(user.getId());
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
        return BookMapper.INSTANCE.map(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The Book is not found", Book.class.getSimpleName(), "Slug", slug)));
    }

    public void updateBook(String slug, String path) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The Book is not found", Book.class.getSimpleName(), "Slug", slug));
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
        return BookMapper.INSTANCE.mapBookRateDto(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The Book is not found", Book.class.getSimpleName(), "Slug", slug)));
    }

    public ReviewDto getBookReviews(String slug) {
        return BookMapper.INSTANCE.getBookReviews(bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The Book is not found", Book.class.getSimpleName(), "Slug", slug)));
    }

    public LocalDate convertToLocalDate(String date) {
        return BookMapper.INSTANCE.convertToLocalDate(date);
    }

    /**
     * Method checks that books are available for purchase:
     * - they are actual
     * - the user has sufficient funds
     * and adds to the books purchased by the user if the conditions are fulfilled
     *
     * @param cookie              - current value cookie, can contain several books at once
     * @param user                - current session user
     * @param httpServletResponse - the response object is where the servlet can write updated Cookie information
     * @return ValidatedResponseDto Dto object used to interact with Front-End to display errors
     */
    public ValidatedResponseDto orderBooks(String cookie, User user, HttpServletResponse httpServletResponse) {
        Map<String, String> errors = new HashMap<>();
        ValidatedResponseDto response = new ValidatedResponseDto(true, errors);

        List<Book> books = findBooksByCookies(cookie);
        removeNotActualBook(response, books, cookie, httpServletResponse);

        int totalPrice = books.stream().mapToInt(Book::getPrice).sum();
        int userBalance = userRepo.getBalance(user.getId());

        if (userBalance >= totalPrice) {
            books.forEach(book -> buyBookByUser(book, user));
            CookieUtil.clearCookieByName(httpServletResponse, "cartContents");
            return new ValidatedResponseDto(true, errors);
        } else {
            errors.put("Balance", "The balance is insufficient to buy books.");
            response.setValidated(false);
        }
        return response;
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
            userRepo.updateBalance(-book.getPrice(), user.getId());

            // TODO: Fix the TransactionAspect to take this part out of the business logic in BookService
            Transaction transaction = new Transaction(-book.getPrice(), TransactionInfo.BUY_BOOK.getValue(), user, book.getId());
            transactionRepo.save(transaction);
        }
    }

    public void viewBookByUser(User user, String slug) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The Book is not found", Book.class.getSimpleName(), "Slug", slug));
        BookToUserType viewed = typeRepo.findByCode(BookToUserEnum.VIEWED);
        boolean isBookAlreadyViewed = bookToUserRepo.existsBookToUserByBookAndUserAndType(book, user, viewed);

        if (!isBookAlreadyViewed) {
            BookToUser viewedBook = new BookToUser(user, book, viewed);
            bookToUserRepo.save(viewedBook);
        }

    }

    private void removeNotActualBook(ValidatedResponseDto response, List<Book> books, String cookie, HttpServletResponse httpServletResponse) {
        books.forEach(book -> {
            if (book.getIsActive() == 0) {
                response.getErrorMessages().put(book.getTitle(), "The book is not available for sale and has been removed from your shopping basket.");
                CookieUtil.removeBookFromCookie(cookie, "cartContents", book.getSlug(), httpServletResponse);
                response.setValidated(false);
            }
        });
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
