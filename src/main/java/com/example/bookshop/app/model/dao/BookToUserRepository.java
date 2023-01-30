package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BookToUserRepository extends JpaRepository<BookToUser, Integer> {

    /*
    Method counts the number of all books that bought, kept, viewed or in user's cart
     */
    long countByBookAndTypeCode(Book book, BookToUserEnum userEnum);

    /*
    Method counts the number of all books that bought, kept, viewed or in user's cart after a specific date
    For example, only books from the last six months can be considered as viewed
    */
    long countByBookAndTypeCodeAndTimeAfter(Book book, BookToUserEnum userEnum, Timestamp time);

    boolean existsBookToUserByBookAndUserAndType(Book book, User user, BookToUserType type);

    Page<BookToUser> findByUserAndType(User user, BookToUserType type, Pageable nextPage);

    List<BookToUser> findByUser(User user);

}
