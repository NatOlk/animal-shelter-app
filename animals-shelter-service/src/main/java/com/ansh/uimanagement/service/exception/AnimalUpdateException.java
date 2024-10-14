package com.ansh.uimanagement.service.exception;


public class AnimalUpdateException extends Exception {
    private String message;

    public AnimalUpdateException(String message) {
       this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
