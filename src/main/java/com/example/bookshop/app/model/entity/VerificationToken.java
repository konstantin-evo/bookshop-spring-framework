package com.example.bookshop.app.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The VerificationToken contains a unique token and an expiry date.
 * Used to confirm user changes to data (password, phone number, email, etc.)
 */
@Getter
@Setter
@Table(name = "verification_token")
@Entity
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    // the nullable = false on the ProfileChanges to ensure data integrity
    // and consistency in the VerificationToken <-> ProfileChanges association.
    @OneToOne(targetEntity = ProfileChanges.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "profile_info_id")
    private ProfileChanges profileChanges;

    private Timestamp expiryDate;

    public VerificationToken(ProfileChanges profileChanges, String token) {
        this.token = token;
        this.profileChanges = profileChanges;
        this.expiryDate = calculateExpiryDate();
    }

    // VerificationToken will expire within 24 hours following its creation
    private Timestamp calculateExpiryDate() {
        return Timestamp.valueOf(LocalDateTime.now().plusDays(1));
    }

}
