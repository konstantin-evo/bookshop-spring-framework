package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookReviewRateRepository;
import com.example.bookshop.app.model.dao.BookReviewRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookReviewRate;
import com.example.bookshop.app.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReviewServiceTest {

    private final static Integer REVIEW_ID = 10;
    private final static Integer REVIEW_VALUE = 1;
    private final static Integer USER_ID = 101;

    @Mock
    BookReviewRateRepository reviewRateRepo;
    @Mock
    BookReviewRepository reviewRepo;
    @Mock
    UserRepository userRepo;

    @InjectMocks
    BookReviewService service;

    @Test
    void setRateBookReviewIfReviewIsExist() {
        when(reviewRateRepo.findByBookReviewAndUser(REVIEW_ID, USER_ID)).thenReturn(mock(BookReviewRate.class));

        boolean result = service.setRateBookReview(REVIEW_ID, REVIEW_VALUE, USER_ID);

        verify(reviewRateRepo, Mockito.times(1))
                .save(any(BookReviewRate.class));
        assertTrue(result);
    }

    @Test
    void setRateBookReviewIfReviewIsNotExist() {
        when(reviewRateRepo.findByBookReviewAndUser(REVIEW_ID, USER_ID)).thenReturn(null);
        when(reviewRepo.findById(REVIEW_ID)).thenReturn(Optional.of(mock(BookReview.class)));
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(mock(User.class)));

        boolean result = service.setRateBookReview(REVIEW_ID, REVIEW_VALUE, USER_ID);

        verify(reviewRateRepo, Mockito.times(1))
                .save(any(BookReviewRate.class));
        assertTrue(result);
    }
}