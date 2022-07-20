package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "transaction")
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp time;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    private Integer value;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String description;

    @Column(columnDefinition = "INT")
    private Integer bookId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private User user;

    public Transaction(Integer value, String description, User user) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.value = value;
        this.description = description;
        this.user = user;
    }

    public Transaction(Integer value, String description, User user, Integer bookId) {
        this(value, description, user);
        this.bookId = bookId;
    }

}
