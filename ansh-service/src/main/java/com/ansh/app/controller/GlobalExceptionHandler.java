package com.ansh.app.controller;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }
}
