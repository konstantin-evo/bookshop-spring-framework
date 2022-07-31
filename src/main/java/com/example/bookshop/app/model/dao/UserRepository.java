package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
    User findUserByPhone(String phone);

    @Query("select u.balance from User u where u.id = :user_id")
    Integer getBalance(@Param("user_id") Integer id);

    @Modifying
    @Query("update User u set u.balance = u.balance + :amount where u.id = :user_id")
    void updateBalance(@Param("amount") Integer sum, @Param("user_id") Integer id);
}
