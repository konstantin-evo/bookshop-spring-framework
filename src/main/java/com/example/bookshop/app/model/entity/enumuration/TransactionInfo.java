package com.example.bookshop.app.model.entity.enumuration;

import lombok.Getter;

public enum TransactionInfo {

    TOPUP("Balance replenished via the user's personal account"),
    BUY_BOOK("The user's balance has been changed due to the purchase of a book");

    @Getter
    private final String value;

    TransactionInfo(String value) {
        this.value = value;
    }
}
