package com.example.bookshop.app.model.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "books")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "book_id_seq", allocationSize = 1)
    private Integer id;

    @Column(columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    // if isBestseller = 1 so the book is considered to be bestseller
    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
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

    /*
    The popularity of a book is a non-negative number
    calculated using a formula based on the number of books bought, kept, viewed, etc.
     */
    @Column(columnDefinition = "FLOAT8 NOT NULL DEFAULT 0")
    private Double popularity;

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

    // The "Popularity" parameters is calculated later,
    // by default they are zero
    public Book() {
        this.popularity = 0.0;
    }

}
