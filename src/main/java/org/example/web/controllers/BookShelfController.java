package org.example.web.controllers;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/books")
@AllArgsConstructor
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        if (!book.getAuthor().isEmpty() && !book.getSize().equals(null) && !book.getTitle().isEmpty()) {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBookById(@RequestParam(value = "bookIdToRemove") String bookIdToRemove) {
        bookService.removeBookById(bookIdToRemove);
        return "redirect:/books/shelf";
    }

    @PostMapping("/removeByRegex")
    public String removeBookByParam(@RequestParam(value = "queryRegex") String queryRegex) {

        if(queryRegex.contains("author=")) {
            String author = queryRegex.replace("author=","");
            bookService.removeBookByAuthor(author);
        }

        if(queryRegex.contains("title=")) {
            String title = queryRegex.replace("title=","");
            bookService.removeBookByTitle(title);
        }

        if(queryRegex.contains("size=")) {
            String size = queryRegex.replace("size=","");
            bookService.removeBookBySize(Integer.valueOf(size));
        }

        return "redirect:/books/shelf";
    }
}
