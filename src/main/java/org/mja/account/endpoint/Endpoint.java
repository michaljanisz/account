package org.mja.account.endpoint;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.mja.account.http.HttpMethod;

public interface Endpoint {


  HttpMethod getMethod();

  String getRequestPath();

  default boolean matches(HttpExchange exchange) {
    return getMethod().name().equals(exchange.getRequestMethod())
        && getRequestPath().equals(exchange.getRequestURI().getPath());
  }

  void handle(HttpExchange httpExchange) throws IOException;

}
