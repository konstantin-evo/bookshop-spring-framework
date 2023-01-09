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
import com.example.bookshop.web.dto.UserReviewDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewService {

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
        BookReviewRate bookReview = reviewRateRepo.findByBookReviewAndUser(reviewId, userId)
                .orElse(createBookReviewRate(reviewId, userId));
        bookReview.setRate(value);
        reviewRateRepo.save(bookReview);
        return true;
    }

    /**
     * Saves in Repository a new review for a Book in case User is not blocked
     *
     * @param slug Unique identifier for the book
     * @param text Review texts
     * @param user User from current session
     *
     * @return a boolean value if the save was successful
     * @throws BookshopEntityNotFoundException in order to save a review, the Book must have Rate and exist
     *                                         if one is absent the exception is thrown
     */
    public boolean saveBookReview(String slug, String text, User user) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));
        BookRate bookRate = bookRateRepo.findBookRateByBookAndUser(book, user)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookRate.class.getSimpleName(), "Book and User", slug));

        return user.getIsBlocked() != 1 && saveBookReview(bookRate, text);
    }

    public List<UserReviewDto> getReviewsByUser(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BookshopEntityNotFoundException(User.class.getSimpleName(), userId));

        List<BookRate> bookRates = bookRateRepo.findBookRateByUser(user);

        if (bookRates.isEmpty()) {
            throw new BookshopEntityNotFoundException(BookRate.class.getSimpleName(), "userId", userId.toString());
        } else {
            return BookReviewMapper.INSTANCE.map(bookRates);
        }
    }

    private boolean saveBookReview(BookRate bookRate, String text) {
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

    private BookReviewRate createBookReviewRate(Integer reviewId, Integer userId) {
        BookReview bookReview = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookReview.class.getSimpleName(), reviewId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BookshopEntityNotFoundException(User.class.getSimpleName(), userId));
        return BookReviewRate.builder()
                .review(bookReview)
                .user(user)
                .build();
    }

}
