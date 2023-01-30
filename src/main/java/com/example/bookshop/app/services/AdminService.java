package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.model.dao.AuthorRepository;
import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookReviewRepository;
import com.example.bookshop.app.model.dao.BookToGenreRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.dao.BookToUserTypeRepository;
import com.example.bookshop.app.model.dao.GenreRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Author;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookReview;
import com.example.bookshop.app.model.entity.BookToGenre;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.Genre;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import com.example.bookshop.web.dto.AuthorDto;
import com.example.bookshop.web.dto.BookCreateDto;
import com.example.bookshop.web.dto.BookToUserDto;
import com.example.bookshop.web.dto.UserDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final BookReviewRepository reviewRepo;
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepo;
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final GenreRepository genreRepo;
    private final TagService tagService;
    private final BookToGenreRepository bookToGenreRepo;
    private final BookToUserRepository bookToUserRepo;
    private final BookToUserTypeRepository bookToUserTypeRepo;
    private final BookToUserMapper bookToUserMapper;

    private static final String DUMMY_IMAGE = "http://dummyimage.com/200x300.png/5fa2dd/ffffff";

    @Transactional
    public ValidatedResponseDto createBook(BookCreateDto bookDto) {

        ValidatedResponseDto response = new ValidatedResponseDto(true, new HashMap<>());

        if (isBookDtoCorrect(bookDto, response)) {
            Book book = bookMapper.map(bookDto);
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
    public ValidatedResponseDto createBookToUser(BookToUserDto bookToUserDto) {

        ValidatedResponseDto response = new ValidatedResponseDto(true, new HashMap<>());

        if (isBookToUserDtoCorrect(bookToUserDto, response)) {

            Book book = bookRepo.findBookBySlug(bookToUserDto.getBookSlug())
                    .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", bookToUserDto.getBookSlug()));
            BookToUserType bookToUserType = bookToUserTypeRepo.findByCode(BookToUserEnum.valueOf(bookToUserDto.getBookStatus()))
                    .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUserType.class.getSimpleName(), "Book Status", BookToUserEnum.VIEWED.toString()));
            User user = userRepo.findUserByName(bookToUserDto.getUsername()).get(0);

            BookToUser bookToUser = new BookToUser(user, book, bookToUserType);

            bookToUserRepo.save(bookToUser);
        }

        return response;
    }

    @Transactional
    public ValidatedResponseDto editBook(BookCreateDto bookDto, String slug) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));

        Author author = authorService.getAuthorByFullName(bookDto.getAuthor())
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Full name", bookDto.getAuthor()));

        bookMapper.updateBook(bookDto, book);
        book.setAuthor(author);
        bookRepo.save(book);
        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto editUser(UserDto userDto, Integer userId, Authentication authentication) {
        User user = userRepo.findById(userId).
                orElseThrow(() -> new BookshopEntityNotFoundException(User.class.getSimpleName(), userId));

        // set updated info in database
        userMapper.updateUser(userDto, user);
        userRepo.save(user);

        // update SecurityContext to display updated information in the current session
        BookshopUserDetails userDetails = (BookshopUserDetails) authentication.getPrincipal();
        userDetails.setIsBlocked(userDto.getIsBlocked());

        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto editBookToUser(BookToUserDto bookToUserDto, Integer bookToUserId) {

        BookToUser bookToUser = bookToUserRepo.findById(bookToUserId)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUser.class.getSimpleName(), bookToUserId));
        BookToUserType bookToUserType = bookToUserTypeRepo.findByCode(BookToUserEnum.valueOf(bookToUserDto.getBookStatus()))
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUserType.class.getSimpleName(), "Book Status", bookToUserDto.getBookStatus()));

        bookToUserMapper.updateBookToUser(bookToUserDto, bookToUser, bookToUserTypeRepo);
        bookToUser.setType(bookToUserType);
        bookToUserRepo.save(bookToUser);

        return new ValidatedResponseDto(true, new HashMap<>());
    }

    @Transactional
    public ValidatedResponseDto editAuthor(AuthorDto authorDto, String slug) {
        Author author = authorRepo.getAuthorBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Author.class.getSimpleName(), "Slug", slug));

        authorMapper.updateAuthor(authorDto, author);
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

    private boolean isBookDtoCorrect(BookCreateDto bookDto, ValidatedResponseDto response) {

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

    private boolean isBookToUserDtoCorrect(BookToUserDto bookToUserDto, ValidatedResponseDto response) {

        List<User> users = userRepo.findUserByName(bookToUserDto.getUsername());
        Optional<Book> book = bookRepo.findBookBySlug(bookToUserDto.getBookSlug());
        Optional<BookToUserType> bookToUserType = bookToUserTypeRepo.findByCode(BookToUserEnum.valueOf(bookToUserDto.getBookStatus()));

        if (users.isEmpty()) {
            response.setValidated(false);
            response.getErrorMessages().put("User", "User not found in database");
        }

        if (users.size() > 1) {
            response.setValidated(false);
            response.getErrorMessages().put("User", "Not uniq username");
        }

        if (book.isEmpty()) {
            response.setValidated(false);
            response.getErrorMessages().put("Book", "Book not find by slug");
        }

        if (bookToUserType.isEmpty()) {
            response.setValidated(false);
            response.getErrorMessages().put("Status", "BookToUserEnum not contained books status");
        }

        return (users.size() == 1) && book.isPresent() && bookToUserType.isPresent();
    }

    private String generateSlug() {
        String slug = "book-" + generateRandomAlphabetic(3) + "-" + generateRandomAlphabetic(3);

        // If a book with this Slug exists, generate a new random value
        while (bookRepo.existsBySlug(slug)) {
            slug = "book-" + generateRandomAlphabetic(3) + "-" + generateRandomAlphabetic(3);
        }

        return slug;
    }

    private String generateRandomAlphabetic(int count) {
        return RandomStringUtils.random(count, 0, 0, true, true, null, new SecureRandom());
    }

}
