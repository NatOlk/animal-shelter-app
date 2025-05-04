package com.ansh.app.service.exception.animal;

import lombok.Getter;

@Getter
public abstract class ShelterException extends RuntimeException {

  protected final String message;

  protected ShelterException(String message) {
    this.message = message;
  }
}
