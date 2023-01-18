package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookToTagRepository extends JpaRepository<BookToTag, Integer>  {

    List<BookToTag> findByBook(Book book);
}
