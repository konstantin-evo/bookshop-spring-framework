package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "book2tag")
@Entity
public class BookToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Tag tag;
}
