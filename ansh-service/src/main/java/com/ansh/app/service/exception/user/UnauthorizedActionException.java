package com.ansh.app.service.exception.user;

public class UnauthorizedActionException extends Exception {
  public UnauthorizedActionException(String message) {
    super(message);
  }
}
