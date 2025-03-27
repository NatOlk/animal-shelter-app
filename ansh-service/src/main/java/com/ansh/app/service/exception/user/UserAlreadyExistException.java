package com.ansh.app.service.exception.user;

public class UserAlreadyExistException extends Exception {

  public UserAlreadyExistException(String message) {
    super(message);
  }
}
