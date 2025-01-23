package com.ansh.app.service.exception;

public class AnimalNotFoundException extends ShelterException {
  public AnimalNotFoundException(String message) {
    super(message);
  }
}
