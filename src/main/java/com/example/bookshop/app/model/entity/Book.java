package com.example.bookshop.app.model.entity;

import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Table(name = "books")
@Entity
public class Book {

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

    @OneToMany(mappedBy="book")
    private List<BookToUser> bookToUsers;

    @OneToMany(mappedBy="book")
    private List<BookToGenre> bookToGenre;

    @OneToMany(mappedBy="book")
    private List<BookToTag> bookToTag;

    @PostPersist
    @PostUpdate
    private void postLoadFunction(){
        long cart = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.CART))
                .count();
        long paid = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.PAID))
                .count();
        long kept = this.bookToUsers.stream()
                .filter(bookToUser -> bookToUser.getType().getCode().equals(BookToUserEnum.KEPT))
                .count();
        this.rating = paid + 0.7 * cart + 0.4 * kept;
    }

}
