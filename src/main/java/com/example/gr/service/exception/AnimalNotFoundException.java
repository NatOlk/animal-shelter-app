package com.example.gr.service.exception;

import java.util.function.Supplier;

public class AnimalNotFoundException extends Exception {
    private String message;

    public AnimalNotFoundException(String message) {
       this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
