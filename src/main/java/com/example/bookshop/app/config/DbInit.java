package com.example.bookshop.app.config;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.entity.Genre;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DbInit {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;
    private final GenreRepository genreRepo;

    @Autowired
    public DbInit(BookRepository bookRepo, BookToUserRepository bookToUserRepo, GenreRepository genreRepo) {
        this.bookRepo = bookRepo;
        this.bookToUserRepo = bookToUserRepo;
        this.genreRepo = genreRepo;
    }

    /**
     * Book rating calculation after Application Startup
     * Further, the rating is updated for each specific book
     *  at the Entity level @PostPersist, @PostUpdate annotation
     */
    @EventListener(ApplicationReadyEvent.class)
    public void calculateBookRatingAfterStartup() {
        bookRepo.findAll().forEach(book -> {
            double cart = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.CART);
            double kept = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.KEPT);
            double paid = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.PAID);
            double rating = paid + 0.7 * cart + 0.4 * kept;
            book.setRating(rating);
            bookRepo.save(book);
        });
    }

    /**
     * Count Of Book By Genre calculation after Application Startup
     * Further, the count is updated for each Genre
     * at the Entity level @PostPersist, @PostUpdate annotation
     */
    @EventListener(ApplicationReadyEvent.class)
    public void calculateCountOfBookByGenreAfterStartup() {
        genreRepo.findAll().forEach(genre -> {
            int result = genre.getBookToGenre().size();

            for (Genre subGenre : genre.getSubGenres()) {
                result += subGenre.getBookToGenre().size();
                if (!subGenre.getSubGenres().isEmpty()) {
                    for (Genre subGenreDeeper : subGenre.getSubGenres()) {
                        result += subGenreDeeper.getBookToGenre().size();
                    }
                }
            }
            genre.setCountOfBook(result);
            genreRepo.save(genre);
        });
    }
}
