package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookToFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookToFileRepository extends JpaRepository<BookToFile,Integer>  {

    BookToFile findBookToFileByHash(String hash);

}
