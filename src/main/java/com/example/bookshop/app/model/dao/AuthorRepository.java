package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Integer> {

}
