package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTest {

    private final BookRepository bookRepository;

    private static final String AUTHOR_TOKEN = "Lizzy";
    private static final String BOOK_TOKEN = "Fish";
    private static final String BOOK_SLUG = "book-bqr-bsi";

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBooksByAuthorFirstNameContaining() {
        List<Book> bookListByAuthorFirstName = bookRepository.findBooksByAuthorFirstNameContaining(AUTHOR_TOKEN);

        assertNotNull(bookListByAuthorFirstName);
        assertFalse(bookListByAuthorFirstName.isEmpty());

        bookListByAuthorFirstName.forEach(book -> assertThat(book.getAuthor().getFirstName()).contains(AUTHOR_TOKEN));

    }

    @Test
    void findBooksByTitleContaining() {
        List<Book> bookListByTitleContaining = bookRepository.findBooksByTitleContaining(BOOK_TOKEN);

        assertNotNull(bookListByTitleContaining);
        assertFalse(bookListByTitleContaining.isEmpty());

        bookListByTitleContaining.forEach(book -> assertThat(book.getTitle()).contains(BOOK_TOKEN));
    }

    @Test
    void findBestsellers() {
        List<Book> bestSellersBooks = bookRepository.findBestsellers();

        assertNotNull(bestSellersBooks);
        assertFalse(bestSellersBooks.isEmpty());
        assertThat(bestSellersBooks.size()).isGreaterThan(1);
    }

    @Test
    void  testBookRating() {
        Book book = bookRepository.findBookBySlug(BOOK_SLUG);
        assertNotNull(book.getRating());
        assertEquals(book.getRating(), 3.2);
    }

}