package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {

}
