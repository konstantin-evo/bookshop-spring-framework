package com.example.bookshop.web.services;

import com.example.bookshop.app.model.dao.BookToFileRepository;
import com.example.bookshop.app.model.entity.BookToFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


@Service
public class ResourceStorage {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private final BookToFileRepository bookFileRepository;

    @Autowired
    public ResourceStorage(BookToFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewBookCover(MultipartFile file, String slug) throws IOException {

        String resourceURI = null;

        if(!file.isEmpty()){

            if(!new File(uploadPath).exists()){
                Files.createDirectories(Paths.get(uploadPath));
                Logger.getLogger(this.getClass()
                        .getSimpleName())
                        .info("Created image folder in " + uploadPath);
            }

            String fileName = slug+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path); //uploading user file here
            Logger.getLogger(this.getClass()
                    .getSimpleName())
                    .info( "The new cover for book is uploaded. File: " + fileName);
        }

        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookToFile bookFile = bookFileRepository.findBookToFileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookToFile bookFile = bookFileRepository.findBookToFileByHash(hash);
        String mimeType =
                URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());

        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        }else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookToFile bookFile = bookFileRepository.findBookToFileByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }
}
