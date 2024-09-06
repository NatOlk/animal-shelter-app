package com.example.gr.service.exception;

public class VaccinationNotFoundException extends Exception {
    private String message;

    public VaccinationNotFoundException(String message) {
       this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
