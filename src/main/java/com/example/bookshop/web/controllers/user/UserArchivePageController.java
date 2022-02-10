package com.example.bookshop.web.controllers.user;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserArchivePageController {

    private final BookService bookService;
    private final UserRegisterService userRegisterService;

    @Autowired
    public UserArchivePageController(BookService bookService,
                                     UserRegisterService userRegisterService) {
        this.bookService = bookService;
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("/archive")
    public String userArchive(Model model) {
        User user = (User) userRegisterService.getCurrentUser();
        model.addAttribute("ArchivedBooks", bookService.getArchivedBooks(user.getEmail()));
        return "archive";
    }
}
