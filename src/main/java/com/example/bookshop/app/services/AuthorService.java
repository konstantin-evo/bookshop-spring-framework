package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.AuthorRepository;
import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepo;
    private final BookMapper bookMapper;

    public AuthorDto getAuthor(Integer id) {
        return bookMapper.map(authorRepo.findById(id)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), id)));
    }

    public AuthorDto getAuthor(String slug) {
        return bookMapper.map(authorRepo.getAuthorBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Slug", slug)));
    }

    public Optional<Author> getAuthorByFullName(String fullName) {
        String[] firstAndLastNames = fullName.split(",");
        if (firstAndLastNames.length >= 2) {
            return authorRepo.getAuthorByFirstNameAndLastName(
                    firstAndLastNames[0].trim(), firstAndLastNames[1].trim());
        } else {
            return Optional.empty();
        }
    }

    /**
     * @return Map<String, List < AuthorDto>> in which the key is a letter of the Latin alphabet,
     * the values are the authors' names and surnames that begin with that letter
     *
     * Used to build a list of authors by letters on an HTML page
     */
    public Map<String, List<AuthorDto>> getAuthorsMap() {
        return getAuthors().stream()
                .collect(Collectors.groupingBy((AuthorDto a) -> a.getLastName().substring(0, 1)));
    }

    private List<AuthorDto> getAuthors() {
        return authorRepo.findAll().stream().map(bookMapper::map)
                .collect(Collectors.toList());
    }

}
