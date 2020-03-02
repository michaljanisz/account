package org.mja.account.http;

import lombok.Getter;

public enum HttpMethod {
  GET(false), POST(true);

  @Getter
  private boolean supportsJsonInRequest;

  HttpMethod(boolean supportsJsonInRequest) {
    this.supportsJsonInRequest = supportsJsonInRequest;
  }
}