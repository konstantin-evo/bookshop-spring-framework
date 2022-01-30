package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findBooksByAuthorFirstNameContaining(String authorsFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByDiscountBetween(Double min, Double max);

    List<Book> findBooksByPriceBetween(Integer min, Integer max);

    List<Book> findBooksByPriceIs(Integer price);

    @Query("from Book where isBestseller=1")
    List<Book> findBestsellers();

    @Query("from Book where discount = (select max(b.discount) from Book b)")
    List<Book> findBooksWithMaxDiscount();

    @Query("select b from Book b join BookToTag bt on b.id = bt.book.id where bt.tag.id = :tag_id")
    Page<Book> findBooksByTag(@Param("tag_id") Integer tagId, Pageable nextPage);

    @Query("select b from Book b join BookToGenre bt on b.id = bt.book.id where bt.genre.id= :genre_id")
    Page<Book> findBooksGenre(@Param("genre_id") Integer id, Pageable nextPage);

    Page<Book> findBooksByAuthorId(Integer id, Pageable nextPage);

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findBookByPubDateIsBetween(Date dateFrom, Date dateTo, Pageable nextPage);

}
