package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "book2user")
@Entity
@NoArgsConstructor
public class BookToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public BookToUser(User user, Book book, BookToUserType type) {
        this.user = user;
        this.book = book;
        this.type = type;
        this.time = new Timestamp(System.currentTimeMillis());
    }

}
