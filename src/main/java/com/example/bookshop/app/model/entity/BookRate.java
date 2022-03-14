package com.example.bookshop.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_rate")
@Entity
public class BookRate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "book_rate_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(columnDefinition = "DATE NOT NULL")
    @Builder.Default
    private Date pubDate = new Date();

    /**
     * Rating is a numerical value from 1 to 5
     */
    @Column(columnDefinition = "SMALLINT NOT NULL")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private User user;

    @OneToOne(mappedBy = "rate")
    private BookReview review;

}
