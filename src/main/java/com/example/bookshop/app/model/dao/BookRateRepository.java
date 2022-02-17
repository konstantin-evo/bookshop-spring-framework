package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRateRepository extends JpaRepository<BookRate,Integer>  {

    Optional<BookRate> findBookRateByBookAndUser(Book book, User user);
}
