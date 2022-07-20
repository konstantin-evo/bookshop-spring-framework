package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Transaction;
import com.example.bookshop.app.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> findByUser(User user, Pageable nextPage);

}
