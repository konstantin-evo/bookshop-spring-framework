package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRateRepository;
import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookRateService {

    private final BookRateRepository bookRateRepo;
    private final UserRepository userRepository;
    private final BookRepository bookRepo;

    @Autowired
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
     * @param userRateJson Book rating value, for example "value=5"
     * @param userId - Unique user ID
     * @param slug unique identifier for the book for set new Rating
     */
    public void setBookRate(String slug, String userRateJson, Integer userId) {
        Book book = bookRepo.findBookBySlug(slug);
        User user = userRepository.findById(userId).orElse(new User());
        List<BookRate> list = bookRateRepo.findBookRateByBookAndUser(book, user);

        int userRate = processingJsonRating(userRateJson);

        if (!list.isEmpty()) {
            list.forEach(bookRate -> {
                bookRate.setRating(userRate);
                bookRate.setPubDate(new Date());
            });
        } else {
            BookRate bookRate = BookRate.builder()
                    .book(book)
                    .user(user)
                    .rating(userRate)
                    .build();
            bookRateRepo.save(bookRate);
        }
    }

    /**
     * Returns the book rating for the specific user
     */
    public Integer getUserRate(String slug, Integer userId) {
        Book book = bookRepo.findBookBySlug(slug);
        User user = userRepository.findById(userId).orElse(new User());
        List<BookRate> list = bookRateRepo.findBookRateByBookAndUser(book, user);
        if (!list.isEmpty()) {
            return list.stream()
                    .filter(bookRate -> bookRate.getUser().equals(user))
                    .mapToInt(BookRate::getRating)
                    .boxed().collect(Collectors.toList())
                    .get(0);
        } else {
            return 0;
        }
    }


    private Integer processingJsonRating(String userRate) {
        userRate = userRate.replace ("value=", "");
        return Integer.valueOf(userRate);
    }
}
