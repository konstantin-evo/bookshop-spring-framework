package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.AuthorDao;
import com.example.bookshop.web.dto.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorDao authorRepo;

    @Autowired
    public AuthorService(AuthorDao authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<AuthorDto> getAuthorsData() {
        return authorRepo.getAll().stream().map(author -> Mapper.INSTANCE.authorToDto(author))
                .collect(Collectors.toList());
    }
}
