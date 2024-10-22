package com.ansh.uimanagement.service.exception;


public class VaccinationUpdateException extends Exception {

  private String message;

  public VaccinationUpdateException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
