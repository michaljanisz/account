package org.mja.account.endpoint.exception;

import org.mja.account.http.HttpStatus;

public class BadRequest extends ProcessingException {

  public BadRequest(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
