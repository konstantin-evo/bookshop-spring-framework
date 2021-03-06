package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserByEmail(String email);
    User findUserByPhone(String phone);
}
