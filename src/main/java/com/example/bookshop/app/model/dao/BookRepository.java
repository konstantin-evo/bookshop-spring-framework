package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findBooksByAuthorFirstNameContaining(String authorsFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByDiscountBetween(Double min, Double max);

    List<Book> findBooksByPriceBetween(Integer min, Integer max);

    List<Book> findBooksByPriceIs(Integer price);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query("from Book where discount = (select max(b.discount) from Book b)")
    List<Book> getBooksWithMaxDiscount();

}
