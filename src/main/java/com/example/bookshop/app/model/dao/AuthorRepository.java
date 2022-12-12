package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> getAuthorByFirstNameAndLastName(String firstName, String lastName);

    Optional<Author> getAuthorBySlug(String slug);

}
