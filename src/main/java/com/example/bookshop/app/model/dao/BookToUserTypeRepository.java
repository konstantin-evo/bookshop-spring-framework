package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookToUserTypeRepository extends JpaRepository<BookToUserType, Integer> {

    Optional<BookToUserType> findByCode(BookToUserEnum code);

}
