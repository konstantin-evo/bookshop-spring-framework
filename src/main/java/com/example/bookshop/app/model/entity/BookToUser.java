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
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "book2user")
@Entity
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

}
