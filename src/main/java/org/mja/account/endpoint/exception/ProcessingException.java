package org.mja.account.endpoint.exception;

import lombok.Getter;
import org.mja.account.endpoint.EndpointResponse;
import org.mja.account.http.HttpStatus;

public class ProcessingException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public ProcessingException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public EndpointResponse toResponse() {
    return EndpointResponse.builder()
        .status(status)
        .responseString(getMessage())
        .build();
  }
}
