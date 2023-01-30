package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookToFileRepository extends JpaRepository<BookToFile,Integer>  {

    BookToFile findBookToFileByHash(String hash);

    List<BookToFile> findBookToFileByBook(Book book);

}
