package org.mja.account.endpoint.exception;

import org.mja.account.http.HttpStatus;

public class BadRequestException extends ProcessingException {

  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
