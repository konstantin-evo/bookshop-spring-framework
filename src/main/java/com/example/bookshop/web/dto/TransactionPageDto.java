package com.example.bookshop.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionPageDto {
    private Integer count;
    private List<TransactionDto> transactions;

    public TransactionPageDto(List<TransactionDto> transactions) {
        this.count = transactions.size();
        this.transactions = transactions;
    }
}
