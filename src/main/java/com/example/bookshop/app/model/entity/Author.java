package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Table(name = "authors")
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
    private String lastName;

    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String photo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @OneToMany(mappedBy = "author")
    private List<Book> bookList = new ArrayList<>();

    @Override
    public String toString() {
        return firstName + ' ' +lastName;
    }

}
