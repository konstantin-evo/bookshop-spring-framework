package com.example.bookshop.app.config;

import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final GenreRepository genreRepo;

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
