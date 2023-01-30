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
    private double bookCoefficientPaid;

    @Value("${book.coefficient.viewed}")
    private double bookCoefficientViewed;

    @Value("${book.coefficient.cart}")
    private double bookCoefficientCart;

    @Value("${book.coefficient.kept}")
    private double bookCoefficientKept;

    @Value("${book.time.popular.month}")
    private int timeOfRelevance;

    @Value("${book.size.popular.update}")
    private int numberOfBooksToUpdate;

    private int currentUpdatingPage;

    /*
    The book's popularity rating is calculated every hour
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void calculateBooksPopularity() {
        Pageable nextPage = PageRequest.of(currentUpdatingPage, numberOfBooksToUpdate);
        Page<Book> books = bookRepo.findActualBooks(nextPage);
        int totalPages = books.getTotalPages();

        if (totalPages > currentUpdatingPage) {
            books.forEach(this::updateBookRating);
            this.currentUpdatingPage++;
            log.info("The Rating of the books has been updated. Total books on the update list: {}, The books have been updated so far: {}, Time: {}",
                    totalPages * numberOfBooksToUpdate, currentUpdatingPage * numberOfBooksToUpdate, LocalDateTime.now());
        } else {
            this.currentUpdatingPage = 0;
            log.info("All the books have been updated. The popularity update process has started from the beginning of the list." +
                    "Time: {}", LocalDateTime.now());
        }
    }

    private void updateBookRating(Book book) {

        Timestamp relevanceTime = Timestamp.valueOf(LocalDateTime.now().minusMonths(timeOfRelevance));

        double cart = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.CART);
        double kept = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.KEPT);
        double paid = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.PAID);
        double viewed = bookToUserRepo.countByBookAndTypeCodeAndTimeAfter(book, BookToUserEnum.VIEWED, relevanceTime);

        double rating = bookCoefficientPaid * paid + bookCoefficientCart * cart + bookCoefficientKept * kept + bookCoefficientViewed * viewed;
        bookRepo.updatePopularity(rating, book.getId());
    }
}
