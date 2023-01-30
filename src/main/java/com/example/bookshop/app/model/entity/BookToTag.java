package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "book2tag")
@Entity
@NoArgsConstructor
public class BookToTag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "book2tag_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Tag tag;

    public BookToTag(Book book, Tag tag) {
        this.book = book;
        this.tag = tag;
    }
}
