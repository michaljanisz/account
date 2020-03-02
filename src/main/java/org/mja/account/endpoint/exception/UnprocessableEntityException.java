package org.mja.account.endpoint.exception;

import org.mja.account.http.HttpStatus;

public class UnprocessableEntityException extends ProcessingException {

  public UnprocessableEntityException(String message) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, message);
  }
}
