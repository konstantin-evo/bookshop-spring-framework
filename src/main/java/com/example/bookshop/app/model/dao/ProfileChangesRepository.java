package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.ProfileChanges;
import com.example.bookshop.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileChangesRepository extends JpaRepository<ProfileChanges, Long> {

    Optional<ProfileChanges> findByUserAndEnabled(User user, boolean enabled);

    @Modifying
    @Query("update ProfileChanges p set p.enabled = :enabled where p.id = :profile_id")
    void updateEnabled(@Param("enabled") boolean enabled, @Param("profile_id") Long id);
}
