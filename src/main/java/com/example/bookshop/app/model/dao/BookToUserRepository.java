package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookToUserRepository extends JpaRepository<BookToUser, Integer> {

    long countByBookAndTypeCode(Book book, BookToUserEnum userEnum);

    boolean existsBookToUserByBookAndUserAndType(Book book, User user, BookToUserType type);

}
