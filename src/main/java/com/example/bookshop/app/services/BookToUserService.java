package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.BookToFileRepository;
import com.example.bookshop.app.model.dao.BookToTagRepository;
import com.example.bookshop.app.model.dao.BookToUserRepository;
import com.example.bookshop.app.model.dao.BookToUserTypeRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.BookToFile;
import com.example.bookshop.app.model.entity.BookToTag;
import com.example.bookshop.app.model.entity.BookToUser;
import com.example.bookshop.app.model.entity.BookToUserType;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.BookToUserEnum;
import com.example.bookshop.web.dto.BookDto;
import com.example.bookshop.web.dto.BookToUserDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import com.example.bookshop.web.exception.UserNotFoundException;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookToUserService {

    private final BookRepository bookRepo;
    private final BookMapper bookMapper;
    private final BookToUserRepository bookToUserRepo;
    private final BookToUserMapper bookToUserMapper;
    private final BookToUserTypeRepository bookToUserTypeRepo;
    private final BookToFileRepository bookToFileRepository;
    private final UserRepository userRepo;
    private final BookToTagRepository bookToTagRepository;

    /**
     * Method checks that books are available for purchase:
     * - they are actual
     * - the user has sufficient funds
     * and adds to the books purchased by the user if the conditions are fulfilled
     *
     * @param cookie              - current value cookie, can contain several books at once
     * @param user                - current session user
     * @param httpServletResponse - the response object is where the servlet can write updated Cookie information
     * @return ValidatedResponseDto Dto object used to interact with Front-End to display errors
     */
    @Transactional
    public ValidatedResponseDto orderBooks(String cookie, User user, HttpServletResponse httpServletResponse) {
        Map<String, String> errors = new HashMap<>();
        ValidatedResponseDto response = new ValidatedResponseDto(true, errors);

        List<String> bookSlugs = CookieUtil.getValues(cookie);
        List<Book> books = bookRepo.findBooksBySlugIn(bookSlugs);
        removeNotActualBook(response, books, cookie, httpServletResponse);

        int totalPrice = books.stream().mapToInt(Book::getPrice).sum();
        int userBalance = userRepo.getBalance(user.getId());

        if (userBalance >= totalPrice) {
            books.forEach(book -> buyBookByUser(book, user));
            CookieUtil.clearCookieByName(httpServletResponse, "cartContents");
            return new ValidatedResponseDto(true, errors);
        } else {
            errors.put("Balance", "The balance is insufficient to buy books.");
            response.setValidated(false);
        }
        return response;
    }

    @Transactional
    public void viewBookByUser(User user, String slug) {
        Book book = bookRepo.findBookBySlug(slug)
                .orElseThrow(() -> new BookshopEntityNotFoundException(Book.class.getSimpleName(), "Slug", slug));
        BookToUserType viewed = bookToUserTypeRepo.findByCode(BookToUserEnum.VIEWED)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUserType.class.getSimpleName(), "BookToUserEnum", BookToUserEnum.VIEWED.toString()));

        boolean isBookAlreadyViewed = bookToUserRepo.existsBookToUserByBookAndUserAndType(book, user, viewed);

        if (!isBookAlreadyViewed) {
            BookToUser viewedBook = new BookToUser(user, book, viewed);
            bookToUserRepo.save(viewedBook);
        }

    }

    public List<BookToUserDto> getBooksByUser(Integer userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<BookToUser> booksToUser = bookToUserRepo.findByUser(user);

        List<BookToUserDto> result = new ArrayList<>();
        booksToUser.forEach(bookToUser -> result.add(bookToUserMapper.map(bookToUser)));

        return result;
    }

    public List<BookDto> getPaidBooks(String userEmail) {
        User user = userRepo.findUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail, true));
        List<Book> books = bookRepo.findPaidBooksByUser(user.getId());

        // set fileList and bookToTags manually
        // to avoid org.hibernate.LazyInitializationException
        books.forEach(book -> {
            List<BookToFile> fileList = bookToFileRepository.findBookToFileByBook(book);
            List<BookToTag> bookToTags = bookToTagRepository.findByBook(book);
            book.setFileList(fileList);
            book.setBookToTag(bookToTags);
        });

        return bookMapper.map(books);
    }

    public List<BookDto> getArchivedBooks(String userEmail) {
        User user = userRepo.findUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail, true));
        List<Book> books = bookRepo.findArchivedBooksByUser(user.getId());
        return bookMapper.map(books);
    }

    /**
     * The method checks if the book was previously purchased by the user.\
     * If not, the book is saved as purchased and the user's balance is updated.
     */
    private void buyBookByUser(Book book, User user) {

        BookToUserType paid = bookToUserTypeRepo.findByCode(BookToUserEnum.PAID)
                .orElseThrow(() -> new BookshopEntityNotFoundException(BookToUserType.class.getSimpleName(), "BookToUserEnum", BookToUserEnum.VIEWED.toString()));

        boolean isBookAlreadyPaid = bookToUserRepo.existsBookToUserByBookAndUserAndType(book, user, paid);

        if (!isBookAlreadyPaid) {
            BookToUser bookToUser = new BookToUser(user, book, paid);
            bookToUserRepo.save(bookToUser);
            userRepo.updateBalance(-book.getPrice(), user.getId());
        }
    }

    private void removeNotActualBook(ValidatedResponseDto response, List<Book> books, String cookie, HttpServletResponse httpServletResponse) {
        books.forEach(book -> {
            if (book.getIsActive() == 0) {
                response.getErrorMessages().put(book.getTitle(), "The book is not available for sale and has been removed from your shopping basket.");
                CookieUtil.removeBookFromCookie(cookie, "cartContents", book.getSlug(), httpServletResponse);
                response.setValidated(false);
            }
        });
    }

}
