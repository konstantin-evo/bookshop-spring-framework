package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "book_review")
@Entity
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "book_review_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp pubDate;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToOne
    @JoinColumn(name = "rate_id", referencedColumnName = "id")
    private BookRate rate;

    @OneToMany(mappedBy = "review")
    private List<BookReviewRate> reviewRates = new ArrayList<>();

    public BookReview() {
        this.pubDate = new Timestamp(System.currentTimeMillis());
    }

    public BookReview(BookRate rate, String text) {
        this();
        this.rate = rate;
        this.text = text;
    }

}
