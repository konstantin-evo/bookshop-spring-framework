package com.example.bookshop.app.aspect;

import com.example.bookshop.app.model.dao.BookRepository;
import com.example.bookshop.app.model.dao.TransactionRepository;
import com.example.bookshop.app.model.entity.Book;
import com.example.bookshop.app.model.entity.Transaction;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.enumuration.TransactionInfo;
import com.example.bookshop.web.services.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Aspect
@Log4j2
@RequiredArgsConstructor
public class TransactionAspect {

    private final TransactionRepository transactionRepo;
    private final BookRepository bookRepo;

    private static final String TOP_UP_BALANCE = TransactionInfo.TOPUP.getValue();
    private static final String ORDER_BOOK = TransactionInfo.BUY_BOOK.getValue();

    @Pointcut(value = "execution(public * com.example.bookshop.app.services.UserProfileService.topUpAccount(..))"
            + "&& args(sum, user, ..)", argNames = "sum, user")
    public void topUpAccount(Integer sum, User user) {
    }

    @Pointcut(value = "execution(public * com.example.bookshop.app.services.BookToUserService.orderBooks(..))"
            + "&& args(cookie, user, httpServletResponse, ..)", argNames = "cookie, user, httpServletResponse")
    public void orderBook(String cookie, User user, HttpServletResponse httpServletResponse) {
    }

    @Transactional
    @After(value = "topUpAccount(sum, user)", argNames = "sum, user")
    public void topUpAccountSuccess(Integer sum, User user) {
        Transaction transaction = new Transaction(sum, TOP_UP_BALANCE, user);
        transactionRepo.save(transaction);
        log.info(TOP_UP_BALANCE + ". User ID: " + user.getId() + ", sum: " + sum);
    }

    @Transactional
    @After(value = "orderBook(cookie, user, httpServletResponse)", argNames = "cookie, user, httpServletResponse")
    public void orderBookSuccess(String cookie, User user, HttpServletResponse httpServletResponse) {

        List<String> bookSlugs = CookieUtil.getValues(cookie);
        List<Book> books = bookRepo.findBooksBySlugIn(bookSlugs);

        books.forEach(book -> {
            Transaction transaction = new Transaction(book.getPrice(), ORDER_BOOK, user, book.getId());
            transactionRepo.save(transaction);
            log.info(ORDER_BOOK + ". User ID: " + user.getId()
                    + " Book ID: " + book.getId() + ", sum: " + book.getPrice());
        });
    }
}
