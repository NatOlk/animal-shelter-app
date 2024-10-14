package com.ansh.uimanagement.service.exception;


public class VaccinationCreationException extends Exception {
    private String message;

    public VaccinationCreationException(String message) {
       this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
