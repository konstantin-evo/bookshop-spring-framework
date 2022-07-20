package com.example.bookshop.app.services;

import com.example.bookshop.app.model.entity.Transaction;
import com.example.bookshop.web.dto.TransactionDto;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@org.mapstruct.Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "value", source = ".", qualifiedByName = "getAmount")
    TransactionDto map(Transaction transaction);

    List<TransactionDto> map(List<Transaction> transactions);

    @Named("getAmount")
    static String getAmount(Transaction transaction) {
        String amount = transaction.getValue().toString();
        return Integer.parseInt(amount) >= 0
                ? "+" + amount + " ₽."
                : amount + " ₽.";
    }

    default String map(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.getDayOfMonth() + " "
                + localDateTime.getMonth().toString().toLowerCase() + " "
                + localDateTime.getYear() + " "
                + localDateTime.getHour() + ":" + localDateTime.getMinute();
    }
}
