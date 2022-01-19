package com.example.bookshop.app.model.entity;

import com.example.bookshop.app.model.entity.enumuration.FileType;
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
@Table(name = "book_file_type")
@Entity
public class BookFileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private FileType name;

    @OneToMany(mappedBy = "bookFileType")
    @Column(columnDefinition = "INT NOT NULL")
    private List<BookFile> bookFiles = new ArrayList<>();

}
