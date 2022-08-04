package com.example.bookshop.app.model.entity;

import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "books")
@Entity
public class Book {

    @Value("${book.coefficient.paid}")
    @Transient
    private double BOOK_COEFFICIENT_PAID;

    @Value("${book.coefficient.viewed}")
    @Transient
    private double BOOK_COEFFICIENT_VIEWED;

    @Value("${book.coefficient.cart}")
    @Transient
    private double BOOK_COEFFICIENT_CART;

    @Value("${book.coefficient.kept}")
    @Transient
    private double BOOK_COEFFICIENT_KEPT;

    @Value("${book.time.popular.month}")
    @Transient
    private int TIME_OF_RELEVANCE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private Integer isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(columnDefinition = "VARCHAR(255)")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer price;

    @Column(columnDefinition = "FLOAT8")
    private Double discount;

    @Column(columnDefinition = "FLOAT8 NOT NULL DEFAULT 0")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<BookToUser> bookToUsers;

    @OneToMany(mappedBy = "book")
    private List<BookToGenre> bookToGenre;

    @OneToMany(mappedBy = "book")
    private List<BookToTag> bookToTag;

    @OneToMany(mappedBy = "book")
    private List<BookToFile> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookRate> bookRates = new ArrayList<>();

    @PostPersist
    private void postLoadFunction() {
        long cart = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.CART))
                .count();
        long paid = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.PAID))
                .count();
        long kept = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.KEPT))
                .count();
        long viewed = this.bookToUsers.stream()
                .filter(bookToUser -> (bookToUser.getType().getCode().equals(BookToUserEnum.VIEWED))
                        & bookToUser.getTime().toLocalDateTime().isAfter(LocalDateTime.now().minusMonths(TIME_OF_RELEVANCE)))
                .count();

        this.rating = BOOK_COEFFICIENT_PAID * paid + BOOK_COEFFICIENT_CART * cart + BOOK_COEFFICIENT_KEPT * kept + BOOK_COEFFICIENT_VIEWED * viewed;
    }

}
