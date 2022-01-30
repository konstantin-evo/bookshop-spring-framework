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
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp regTime;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy="user")
    private List<BookToUser> bookToUsers;

}
