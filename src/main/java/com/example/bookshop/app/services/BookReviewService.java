package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRateRepository;
import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookReviewRateRepository;
import com.example.bookshop.app.model.dao.BookReviewRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookReviewRate;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.BookRateNotFoundException;
import com.example.bookshop.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final static String ERROR_MESSAGE = "No rating found for this review. Please rate the book first.";

    private final BookReviewRateRepository reviewRateRepo;
    private final BookReviewRepository reviewRepo;
    private final BookRateRepository bookRateRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    /**
     * The method updates the "Like", "Dislike" counter for a specific review
     * If the User has previously rated the Review - the value is updated
     * If the User has not previously rated the Review - a new "Like-Dislike" is created
     *
     * @param value    defines "Like" (1), or "Dislike" (-1)
     * @param userId   unique user ID
     * @param reviewId unique review ID
     * @return true if the rating update was successful
     */
    public boolean setRateBookReview(Integer reviewId, Integer value, Integer userId) {

        if (reviewRateRepo.findByBookReviewAndUser(reviewId, userId) != null) {
            BookReviewRate bookReview = reviewRateRepo.findByBookReviewAndUser(reviewId, userId);
            bookReview.setRate(value);
            reviewRateRepo.save(bookReview);
        } else {
            BookReview bookReview = reviewRepo.findById(reviewId).orElse(null);
            User user = userRepo.findById(userId).orElse(null);
            reviewRateRepo.save(
                    BookReviewRate.builder()
                            .review(bookReview)
                            .user(user)
                            .rate(value)
                            .build());
        }
        return true;
    }

    /**
     * Saves in Repository a new review for a Book
     *
     * @param slug   Unique identifier for the book
     * @param text   Review texts
     * @param userId Unique user ID
     * @return a boolean value if the save was successful
     *
     * @throws BookRateNotFoundException in order to save a review, the Book must have Rate,
     *                                   if Rate is absent, the Exception is returned (relevant for API)
     */
    public boolean saveBookReview(String slug, String text, Integer userId) throws BookRateNotFoundException {
        Book book = bookRepo.findBookBySlug(slug);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        BookRate bookRate = bookRateRepo.findBookRateByBookAndUser(book, user)
                .orElseThrow(() -> new BookRateNotFoundException(ERROR_MESSAGE));

        if (reviewRepo.findByRate(bookRate) != null) {
            BookReview bookReview = reviewRepo.findByRate(bookRate);
            bookReview.setText(text);
            bookReview.setPubDate(new Timestamp(System.currentTimeMillis()));
            reviewRepo.save(bookReview);
        } else {
            BookReview bookReview = new BookReview(bookRate, text);
            bookRate.setReview(new BookReview(bookRate, text));
            reviewRepo.save(bookReview);
        }
        return true;
    }

}
