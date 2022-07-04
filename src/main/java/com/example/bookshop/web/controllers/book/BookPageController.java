package com.example.bookshop.web.controllers.book;

import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.services.BookRateService;
import com.example.bookshop.app.services.BookService;
import com.example.bookshop.app.services.UserRegisterService;
import com.example.bookshop.web.services.ResourceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Log4j2
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookPageController {

    private final BookService bookService;
    private final BookRateService rateService;
    private final UserRegisterService userRegisterService;
    private final ResourceStorage storage;

    @GetMapping("/{slug}")
    public String mainBookPage(@PathVariable String slug, Model model) {

        model.addAttribute("book", bookService.getBook(slug));
        model.addAttribute("bookRate", bookService.getBookRate(slug));
        model.addAttribute("reviews", bookService.getBookReviews(slug));

        User user = (User) userRegisterService.getCurrentUser();

        if (user != null) {
            model.addAttribute("userRate", rateService.getUserRate(slug, user.getId()));
        }

        return "books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {
        String path = storage.saveNewBookCover(file, slug);
        bookService.updateBook(slug, path);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        log.info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        log.info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        log.info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

}
