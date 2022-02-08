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

@Getter
@Setter
@Table(name = "book2file")
@Entity
public class BookToFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;
}
