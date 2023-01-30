package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> getGenreByName(String name);

}
