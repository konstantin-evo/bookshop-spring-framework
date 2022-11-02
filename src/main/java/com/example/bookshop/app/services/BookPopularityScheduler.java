package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Log4j2
public class BookPopularityScheduler {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;

    @Value("${book.coefficient.paid}")
    private double BOOK_COEFFICIENT_PAID;

    @Value("${book.coefficient.viewed}")
    private double BOOK_COEFFICIENT_VIEWED;

    @Value("${book.coefficient.cart}")
    private double BOOK_COEFFICIENT_CART;

    @Value("${book.coefficient.kept}")
    private double BOOK_COEFFICIENT_KEPT;

    @Value("${book.time.popular.month}")
    private int TIME_OF_RELEVANCE;

    @Value("${book.size.popular.update}")
    private int NUMBER_OF_BOOKS_TO_UPDATE;

    private static int CURRENT_UPDATING_PAGE;

    /*
    The book's popularity rating is calculated every hour
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void calculateBooksPopularity() {
        Pageable nextPage = PageRequest.of(CURRENT_UPDATING_PAGE, NUMBER_OF_BOOKS_TO_UPDATE);
        Page<Book> books = bookRepo.findActualBooks(nextPage);
        int totalPages = books.getTotalPages();

        if (totalPages > CURRENT_UPDATING_PAGE) {
            books.forEach(this::updateBookRating);
            CURRENT_UPDATING_PAGE++;
            log.info("The Rating of the books has been updated. Total books on the update list: {}, The books have been updated so far: {}, Time: {}",
                    totalPages * NUMBER_OF_BOOKS_TO_UPDATE, CURRENT_UPDATING_PAGE * NUMBER_OF_BOOKS_TO_UPDATE, LocalDateTime.now());
        } else {
            CURRENT_UPDATING_PAGE = 0;
            log.info("All the books have been updated. The popularity update process has started from the beginning of the list." +
                    "Time: {}", LocalDateTime.now());
        }
    }

    private void updateBookRating(Book book) {

        Timestamp relevanceTime = Timestamp.valueOf(LocalDateTime.now().minusMonths(TIME_OF_RELEVANCE));

        double cart = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.CART);
        double kept = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.KEPT);
        double paid = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.PAID);
        double viewed = bookToUserRepo.countByBookAndTypeCodeAndTimeAfter(book, BookToUserEnum.VIEWED, relevanceTime);

        double rating = BOOK_COEFFICIENT_PAID * paid + BOOK_COEFFICIENT_CART * cart + BOOK_COEFFICIENT_KEPT * kept + BOOK_COEFFICIENT_VIEWED * viewed;
        bookRepo.updatePopularity(rating, book.getId());
    }
}
