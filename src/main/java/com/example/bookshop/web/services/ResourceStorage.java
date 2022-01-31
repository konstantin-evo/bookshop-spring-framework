package com.example.bookshop.web.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


@Service
public class ResourceStorage {

    @Value("${upload.path}")
    String uploadPath;

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
}
