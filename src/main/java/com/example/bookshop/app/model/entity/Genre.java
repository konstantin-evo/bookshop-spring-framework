package com.example.bookshop.app.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "genre")
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @JsonIgnore
    private String slug;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Genre parent;

    private Integer countOfBook;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Genre> subGenres = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "genre")
    @JsonIgnore
    private List<BookToGenre> bookToGenre;

    @PostPersist
    @PostUpdate
    private void calculateCountOfBook() {
        int result = this.getBookToGenre().size();

        for (Genre subGenre : this.getSubGenres()) {
            result += subGenre.getBookToGenre().size();
            if (! subGenre.getSubGenres().isEmpty()) {
                for (Genre subGenreDeeper : subGenre.getSubGenres()) {
                    result += subGenreDeeper.getBookToGenre().size();
                }
            }
        }
        this.countOfBook = result;
    }
}
