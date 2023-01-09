package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.model.dao.AuthorRepository;
import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookReviewRepository;
import com.example.bookshop.app.model.dao.BookToGenreRepository;
import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookToGenre;
import com.example.bookshop.app.model.entity.Genre;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookCreateDto;
import com.example.bookshop.web.dto.UserDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookRepository bookRepo;
    private final BookReviewRepository reviewRepo;
    private final AuthorService authorService;
    private final AuthorRepository authorRepo;
    private final UserRepository userRepo;
    private final GenreRepository genreRepo;
    private final TagService tagService;
    private final BookToGenreRepository bookToGenreRepo;

    private static final String DUMMY_IMAGE = "http://dummyimage.com/200x300.png/5fa2dd/ffffff";

    public ValidatedResponseDto createBook(BookCreateDto bookDto) {

        ValidatedResponseDto response = new ValidatedResponseDto(true, new HashMap<>());

        if (isDtoCorrect(bookDto, response)) {
            Book book = BookMapper.INSTANCE.map(bookDto);
            Author author = authorService.getAuthorByFullName(bookDto.getAuthor())
                    .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Full name", bookDto.getAuthor()));

            book.setAuthor(author);
            book.setSlug(generateSlug());
            book.setImage(DUMMY_IMAGE);
            bookRepo.save(book);

            Genre genre = genreRepo.getGenreByName(bookDto.getGenre())
                    .orElseThrow(() -> new BookshopEntityNotFoundException(Genre.class.getSimpleName(), "Name", bookDto.getGenre()));

            bookToGenreRepo.save(new BookToGenre(book, genre));

            if (!bookDto.getTags().isEmpty()) {
                tagService.setTagsToBook(bookDto.getTags(), book);
            }
        }

        return response;
    }

    @Transactional
    public ValidatedResponseDto editBook(BookCreateDto bookDto, String slug) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));

        Author author = authorService.getAuthorByFullName(bookDto.getAuthor())
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Full name", bookDto.getAuthor()));

        BookMapper.INSTANCE.updateBook(bookDto, book);
        book.setAuthor(author);
        bookRepo.save(book);
        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto editUser(UserDto userDto, Integer userId, Authentication authentication) {
        User user = userRepo.findById(userId).
                orElseThrow(() -> new BookshopEntityNotFoundException(User.class.getSimpleName(), userId));

        // set updated info in database
        UserMapper.INSTANCE.updateUser(userDto, user);
        userRepo.save(user);

        // update SecurityContext to display updated information in the current session
        BookshopUserDetails userDetails = (BookshopUserDetails) authentication.getPrincipal();
        userDetails.setIsBlocked(userDto.getIsBlocked());

        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto editAuthor(AuthorDto authorDto, String slug) {
        Author author = authorRepo.getAuthorBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Slug", slug));

        AuthorMapper.INSTANCE.updateAuthor(authorDto, author);
        authorRepo.save(author);
        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto deleteBook(String slug) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));
        bookRepo.updateIsActive(0, book.getId());
        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto deleteReview(Integer reviewId) {

        Optional<BookReview> bookReview = reviewRepo.findById(reviewId);

        if (bookReview.isPresent()) {
            reviewRepo.updateIsActive(0, reviewId);
            return new ValidatedResponseDto(true, new HashMap<>());
        } else {
            throw new BookshopEntityNotFoundException(BookReview.class.getSimpleName(), reviewId);
        }
    }

    private boolean isDtoCorrect(BookCreateDto bookDto, ValidatedResponseDto response) {

        Optional<Author> author = authorService.getAuthorByFullName(bookDto.getAuthor());
        Optional<Genre> genre = genreRepo.getGenreByName(bookDto.getGenre());

        if (author.isEmpty()) {
            response.setValidated(false);
            response.getErrorMessages().put("Author", "Author not found in database");
        }

        if (genre.isEmpty()) {
            response.setValidated(false);
            response.getErrorMessages().put("Genre", "Genre not found in database");
        }

        boolean isTagsCorrect = bookDto.getTags().isEmpty() || tagService.areTagsExist(bookDto.getTags());

        if (!isTagsCorrect) {
            response.setValidated(false);
            response.getErrorMessages().put("Tag", "Tag not found in database");
        }

        return author.isPresent() && genre.isPresent() && isTagsCorrect;
    }

    private String generateSlug() {
        String slug = "book-"
                + RandomStringUtils.randomAlphabetic(3) + "-"
                + RandomStringUtils.randomAlphabetic(3);

        // If a book with this Slug exists, generate a new random value
        while (bookRepo.existsBySlug(slug)) {
            slug = "book-"
                    + RandomStringUtils.randomAlphabetic(3) + "-"
                    + RandomStringUtils.randomAlphabetic(3);
        }
        return slug;
    }

}
