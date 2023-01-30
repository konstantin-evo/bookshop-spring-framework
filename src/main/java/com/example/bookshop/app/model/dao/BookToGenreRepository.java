package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookToGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookToGenreRepository extends JpaRepository<BookToGenre, Integer>  {
}
