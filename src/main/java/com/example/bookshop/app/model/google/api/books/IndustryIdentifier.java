package com.example.bookshop.app.model.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Designed to interact with GoogleBook API
 *
 * Industry standard identifiers for this volume.
 */
public class IndustryIdentifier {
    @JsonProperty("type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    @JsonProperty("identifier")
    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    String identifier;
}