package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByAuthorFirstNameContaining(String authorsFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByDiscountBetween(Double min, Double max);

    List<Book> findBooksByPriceBetween(Integer min, Integer max);

    List<Book> findBooksByPriceIs(Integer price);

    List<Book> findBooksBySlugIn(Collection<String> slug);

    @Query("from Book where isBestseller=1")
    List<Book> findBestsellers();

    @Query("from Book where discount = (select max(b.discount) from Book b)")
    List<Book> findBooksWithMaxDiscount();

    @Query("select b from Book b join BookToUser bt on b.id = bt.book.id where bt.type.id = 3 and bt.user.id = :user_id")
    List<Book> findPaidBooksByUser(@Param("user_id") Integer userId);

    @Query("select b from Book b join BookToUser bt on b.id = bt.book.id where bt.type.id = 4 and bt.user.id = :user_id")
    List<Book> findArchivedBooksByUser(@Param("user_id") Integer userId);

    @Query("select b from Book b join BookToTag bt on b.id = bt.book.id where bt.tag.id = :tag_id")
    Page<Book> findBooksByTag(@Param("tag_id") Integer tagId, Pageable nextPage);

    @Query("select b from Book b join BookToGenre bt on b.id = bt.book.id where bt.genre.id= :genre_id")
    Page<Book> findBooksGenre(@Param("genre_id") Integer id, Pageable nextPage);

    Page<Book> findBooksByAuthorId(Integer id, Pageable nextPage);

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findBookByPubDateIsBetween(Date dateFrom, Date dateTo, Pageable nextPage);

    Book findBookBySlug(String slug);

    @Modifying
    @Query("update Book b set b.rating = b.rating + :amount where b.id = :book_id")
    void updateRating(@Param("amount") Double amount, @Param("book_id") Integer id);
}
