package com.ansh.management.service.exception;

import lombok.Getter;

@Getter
public abstract class ShelterException extends Exception {

  protected final String message;

  protected ShelterException(String message) {
    this.message = message;
  }
}
