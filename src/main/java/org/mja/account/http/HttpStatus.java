package org.mja.account.http;

import lombok.Getter;

public enum HttpStatus {
  OK(200), NOT_FOUND(404), UNPROCESSABLE_ENTITY(422), INTERNAL_SERVER_ERROR(500);
  @Getter
  private int code;

  HttpStatus(int code) {
    this.code = code;
  }
}