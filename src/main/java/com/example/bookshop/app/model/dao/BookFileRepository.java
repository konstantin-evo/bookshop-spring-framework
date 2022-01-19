package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile,Integer> {
}
