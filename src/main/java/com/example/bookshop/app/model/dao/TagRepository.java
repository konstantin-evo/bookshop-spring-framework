package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> getTagByName(String name);
    boolean existsByNameIgnoreCase(String name);

}
