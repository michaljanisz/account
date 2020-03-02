package org.mja.account.endpoint.exception;

import org.mja.account.http.HttpStatus;

public class NotFoundException extends ProcessingException {

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
