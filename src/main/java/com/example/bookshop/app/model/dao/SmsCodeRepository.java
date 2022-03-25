package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode,Long> {

    SmsCode findByCode(String code);
}
