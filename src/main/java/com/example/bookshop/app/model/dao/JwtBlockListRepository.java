package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.JwtBlockList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlockListRepository extends JpaRepository<JwtBlockList, Integer>  {

    boolean existsByToken(String token);

}
