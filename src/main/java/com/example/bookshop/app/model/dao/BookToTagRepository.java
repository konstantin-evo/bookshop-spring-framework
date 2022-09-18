package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookToTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookToTagRepository extends JpaRepository<BookToTag, Integer>  {
}
