package com.example.gr.service.exception;


public class AnimalCreationException extends Exception {
    private String message;

    public AnimalCreationException(String message) {
       this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
