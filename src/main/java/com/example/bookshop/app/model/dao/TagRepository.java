package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {

}
