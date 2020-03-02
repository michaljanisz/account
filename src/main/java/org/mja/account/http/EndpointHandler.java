package org.mja.account.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Singleton;
import org.mja.account.endpoint.Endpoint;
import org.mja.account.util.HtmlUtil;

@Singleton
public class EndpointHandler implements HttpHandler {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private Collection<Endpoint> endpoints;

  public EndpointHandler(Set<Endpoint> endpoints) {
    this.endpoints = endpoints;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    try {
      for (Endpoint endpoint : endpoints) {
        if (endpoint.matches(httpExchange)) {
          endpoint.handle(httpExchange);
          return;
        }
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "unexpected error", e);
      HtmlUtil
          .sendResponse(httpExchange, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
      return;
    }
    HtmlUtil.sendResponse(httpExchange, "not found", HttpStatus.NOT_FOUND);
  }
}