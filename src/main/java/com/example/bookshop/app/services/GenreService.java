package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepo;

    @Autowired
    public GenreService(GenreRepository genreRepo) {
        this.genreRepo = genreRepo;
    }

    public Genre getGenreData(Integer id) {
        return genreRepo.findById(id).get();
    }

    public List<Genre> getGenresData() {
        return genreRepo.findAll().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }
}
