package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRateRepository extends JpaRepository<BookRate,Integer>  {

    List<BookRate> findBookRateByBookAndUser(Book book, User user);
}
