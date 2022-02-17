package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

}
