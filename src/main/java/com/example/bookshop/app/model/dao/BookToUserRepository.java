package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookToUserRepository extends JpaRepository<BookToUser,Integer> {

    long countByBookAndTypeCode(Book book, BookToUserEnum userEnum);

}
