package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * The table is used to temporarily store user's profile data
 * Once the user data has been confirmed by email, it will be stored in the "User" table
 */
@Getter
@Setter
@Table(name = "profile_changes")
@Entity
public class ProfileChanges {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    private String email;
    private String phone;
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private User user;

    /**
     * The value of the "enabled" field is false by default.
     * The service layer changes it to true if the user successfully confirms the Profile changes.
     */
    public ProfileChanges() {
        super();
        this.enabled = false;
    }
}
