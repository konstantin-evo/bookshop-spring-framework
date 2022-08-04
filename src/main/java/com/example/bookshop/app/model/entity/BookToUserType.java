package com.example.bookshop.app.model.entity;

import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Table(name = "book2user_type")
@Entity
public class BookToUserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private BookToUserEnum code;

    @OneToMany(mappedBy="type")
    private List<BookToUser> bookToUsers;
}
