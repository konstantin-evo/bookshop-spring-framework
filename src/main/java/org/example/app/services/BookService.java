package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.apache.log4j.Logger;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(String bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeBookByAuthor(String author) {
        return bookRepo.removeBookByAuthor(author);
    }

    public boolean removeBookByTitle(String title) {
        return bookRepo.removeBookByTitle(title);
    }

    public boolean removeBookBySize(Integer size) {
        return bookRepo.removeBookBySize(size);
    }

    private void defaultInit() {
        logger.info("default INIT in book service");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in book service");
    }
}
