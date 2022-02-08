package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookToFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookToFileRepository extends JpaRepository<BookToFile,Integer>  {

    BookToFile findBookToFileByHash(String hash);

}
