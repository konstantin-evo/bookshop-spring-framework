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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
