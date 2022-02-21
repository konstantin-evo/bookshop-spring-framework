package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests are written according to the course materials,
 * in the future it should be replaced with @DataJpaTest
 */
@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBooksByAuthorFirstNameContaining() {
        String token = "Lizzy";
        List<Book> bookListByAuthorFirstName = bookRepository.findBooksByAuthorFirstNameContaining(token);

        assertNotNull(bookListByAuthorFirstName);
        assertFalse(bookListByAuthorFirstName.isEmpty());

        bookListByAuthorFirstName.forEach(book -> {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.getTitle());
            assertThat(book.getAuthor().getFirstName()).contains(token);
        });

    }

    @Test
    void findBooksByTitleContaining() {
        String token = "Fish";
        List<Book> bookListByTitleContaining = bookRepository.findBooksByTitleContaining(token);

        assertNotNull(bookListByTitleContaining);
        assertFalse(bookListByTitleContaining.isEmpty());

        bookListByTitleContaining.forEach(book -> {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.getTitle());
            assertThat(book.getTitle()).contains(token);
        });
    }

    @Test
    void findBestsellers() {
        List<Book> bestSellersBooks = bookRepository.findBestsellers();

        assertNotNull(bestSellersBooks);
        assertFalse(bestSellersBooks.isEmpty());
        assertThat(bestSellersBooks.size()).isGreaterThan(1);
    }

}