package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookReviewRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookReviewRateRepository extends JpaRepository<BookReviewRate,Integer>  {

    @Query("from BookReviewRate b where b.review.id = :review_id and b.user.id = :user_id")
    BookReviewRate findByBookReviewAndUser(@Param("review_id") Integer reviewId,
                                           @Param("user_id") Integer userId);
}
