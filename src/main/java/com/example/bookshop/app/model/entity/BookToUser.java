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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "book2user")
@Entity
public class BookToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "book2user_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private BookToUserType type;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private User user;

    public BookToUser() {
        this.time = new Timestamp(System.currentTimeMillis());
    }
    public BookToUser(User user, Book book, BookToUserType type) {
        this();
        this.user = user;
        this.book = book;
        this.type = type;
    }

}
