package org.mja.account.endpoint;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.mja.account.http.HttpMethod;

public interface Endpoint {

  HttpMethod getMethod();

  String getRequestPath();

  default boolean matches(HttpExchange exchange) {
    var path = exchange.getRequestURI().getPath();
    if (path.contains("?")) {
      path = path.split("\\?")[0];
    }
    if (path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    return getMethod().name().equals(exchange.getRequestMethod())
        && path.startsWith(getRequestPath());
  }

  void handle(HttpExchange httpExchange) throws IOException;

}
