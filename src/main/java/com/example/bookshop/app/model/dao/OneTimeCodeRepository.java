package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.OneTimeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimeCodeRepository extends JpaRepository<OneTimeCode,Long> {

    OneTimeCode findByCode(String code);
}
