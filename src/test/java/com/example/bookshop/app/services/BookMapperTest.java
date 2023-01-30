package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.BookRateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {
    @Spy
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    private Book book;
    private BookRateDto bookRate;

    @BeforeEach
    void setUp() {
        book = generateBook();
        bookRate = bookMapper.mapBookRateDto(book);
    }

    /**
     * Test checks the  of the Book based on the Users' assessment
     *
     * Rating is an integer from 1 to 5 that represents the rating of the book (rounded up)
     * if the book has no ratings, returns 5 (maximum rating)
     */
    @Test
    void testBookRate() {
        assertEquals(3, bookRate.getRate());
    }

    /**
     * Test checks Rate Distribution based on the Users' assessment
     * <p>
     * Rate Distribution is a Map<Integer, Integer> which contains the key "Rating" from 1 to 5
     * and the value "Count", which shows the number of users who gave this rating
     */
    @Test
    void testBookRateDistribution() {
        assertEquals(1, bookRate.getRateDistribution().get(1));
        assertEquals(1, bookRate.getRateDistribution().get(2));
        assertEquals(1, bookRate.getRateDistribution().get(3));
        assertEquals(1, bookRate.getRateDistribution().get(4));
        assertEquals(1, bookRate.getRateDistribution().get(5));
    }

    private Book generateBook() {
        Book book = new Book();
        book.setBookRates(generateBookRates());
        return book;
    }

    private List<BookRate> generateBookRates() {
        List<BookRate> bookRates = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            bookRates.add(BookRate.builder()
                    .book(book)
                    .rating(i)
                    .user(new User())
                    .build());
        }
        return bookRates;
    }

}