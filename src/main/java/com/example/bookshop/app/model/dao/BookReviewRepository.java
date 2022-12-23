package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookRate;
import com.example.bookshop.app.model.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookReviewRepository extends JpaRepository<BookReview,Integer>  {

    BookReview findByRate(BookRate rate);

    @Modifying
    @Query("update BookReview b set b.isActive = :isActive where b.id = :id")
    void updateIsActive(@Param("isActive") Integer isActive, @Param("id") Integer id);
}
