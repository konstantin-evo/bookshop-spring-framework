package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "user_id_seq", allocationSize = 1)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Timestamp regTime;

    @Column(name = "balance", columnDefinition = "INTEGER DEFAULT 0")
    private Integer balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    private String email;
    private String phone;
    private String password;

    // if isBlocked = 1 so the User can't write Reviews
    // using by the Admin role to moderate reviews
    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 1")
    private Integer isBlocked;

    @OneToMany(mappedBy = "user")
    private List<BookToUser> bookToUsers;

    @OneToMany(mappedBy = "user")
    private List<BookRate> userRates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookReviewRate> reviewRates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ProfileChanges> profileChanges = new ArrayList<>();

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user2role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
        String userHash = String.valueOf(this.hashCode());
        this.hash = "user-" + userHash.substring(0, 2) + "-" + userHash.substring(3, 6);
        this.regTime = new Timestamp(System.currentTimeMillis());
        this.balance = 0;
        this.isBlocked = 0;
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

}
