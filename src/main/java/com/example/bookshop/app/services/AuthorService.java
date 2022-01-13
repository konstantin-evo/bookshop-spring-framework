package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.AuthorDao;
import com.example.bookshop.web.dto.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorDao authorRepo;

    @Autowired
    public AuthorService(AuthorDao authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<AuthorDto> getAuthors() {
        return authorRepo.getAll().stream().map(Mapper.INSTANCE::authorToDto)
                .collect(Collectors.toList());
    }

    public Map<String, List<AuthorDto>> getAuthorsMap(){
        return getAuthors().stream()
                .collect(Collectors.groupingBy((AuthorDto a)-> a.getSurname().substring(0,1)));
    }
}
