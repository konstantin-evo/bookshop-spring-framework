package com.example.bookshop.app.model.entity.enumuration;

import lombok.Getter;

public enum FileTypeEnum {

    PDF(".pdf"),
    EPUB(".epub"),
    FB2(".fb2");

    @Getter
    private final String fileExtensionString;

    FileTypeEnum(String fileExtensionString) {
        this.fileExtensionString = fileExtensionString;
    }

}
