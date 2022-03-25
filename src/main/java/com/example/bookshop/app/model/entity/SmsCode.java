package com.example.bookshop.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDateTime expireTime;

    public SmsCode(String code, Integer expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public Boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
