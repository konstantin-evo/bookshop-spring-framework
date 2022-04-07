package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.AuthorRepository;
import com.example.bookshop.web.dto.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepo;

    @Autowired
    public AuthorService(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    public AuthorDto getAuthor(Integer id){
        return BookMapper.INSTANCE.map(authorRepo.findById(id).orElse(null));
    }

    public List<AuthorDto> getAuthors() {
        return authorRepo.findAll().stream().map(BookMapper.INSTANCE::map)
                .collect(Collectors.toList());
    }

    public Map<String, List<AuthorDto>> getAuthorsMap(){
        return getAuthors().stream()
                .collect(Collectors.groupingBy((AuthorDto a)-> a.getLastName().substring(0,1)));
    }

}
