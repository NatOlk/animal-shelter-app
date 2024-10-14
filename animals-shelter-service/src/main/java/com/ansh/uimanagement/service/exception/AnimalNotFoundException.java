package com.ansh.uimanagement.service.exception;

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
