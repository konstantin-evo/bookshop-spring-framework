package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.TransactionRepository;
import com.example.bookshop.app.model.entity.Transaction;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;

    public Page<TransactionDto> getPageOfTransaction(User user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by("time").descending());
        Page<Transaction> transactions = transactionRepo.findByUser(user, nextPage);
        List<TransactionDto> transactionDto = TransactionMapper.INSTANCE.map(transactions.getContent());
        return new PageImpl<>(transactionDto, nextPage, transactions.getTotalElements());
    }
}
