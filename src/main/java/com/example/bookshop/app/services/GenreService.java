package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.entity.Genre;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepo;

    public Genre getGenreData(Integer id) throws BookshopEntityNotFoundException {
        return genreRepo.findById(id)
                .orElseThrow(() -> new BookshopEntityNotFoundException("The genre is not found", Genre.class.getSimpleName(), "id", id.toString()));
    }

    public List<Genre> getGenresData() {
        return genreRepo.findAll().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    public List<String> getGenresName() {
        return genreRepo.findAll().stream()
                .filter(genre -> genre.getParent() != null)
                .map(Genre::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
