package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRateRepository;
import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class BookRateService {

    private final BookRateRepository bookRateRepo;
    private final UserRepository userRepository;
    private final BookRepository bookRepo;

    public BookRateService(BookRateRepository bookRateRepo, UserRepository userRepository, BookRepository bookRepo) {
        this.bookRateRepo = bookRateRepo;
        this.userRepository = userRepository;
        this.bookRepo = bookRepo;
    }

    /**
     * The method sets or updates the Book Rating on behalf of the current user
     * If the rating for this book was set earlier, the field is updated
     * If the rating for this book is set to the first, a new record is created in the database
     *
     * @param userRate Book rating value, for example "value=5"
     * @param userId   Unique user ID
     * @param slug     unique identifier for the book for set new Rating
     */
    public boolean setBookRate(String slug, Integer userRate, Integer userId) {
        Book book = bookRepo.findBookBySlug(slug);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BookRate bookRate = bookRateRepo.findBookRateByBookAndUser(book, user)
                .orElse(createBookRate(book, user));

        bookRate.setRating(userRate);
        bookRate.setPubDate(new Date());
        bookRateRepo.save(bookRate);
        return true;
    }

    /**
     * Returns the book rating for the specific user
     */
    public Integer getUserRate(String slug, Integer userId) {
        Book book = bookRepo.findBookBySlug(slug);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BookRate bookRate = bookRateRepo.findBookRateByBookAndUser(book, user).orElse(null);
        return (bookRate != null) ? bookRate.getRating() : 0;
    }

    private BookRate createBookRate(Book book, User user) {
        return BookRate.builder()
                .book(book)
                .user(user)
                .build();
    }

}
