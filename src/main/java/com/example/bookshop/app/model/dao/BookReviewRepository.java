package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReview,Integer>  {

    BookReview findByRate(BookRate rate);
}
