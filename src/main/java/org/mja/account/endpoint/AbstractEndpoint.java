package org.mja.account.endpoint;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Logger;
import mjson.Json;
import org.mja.account.endpoint.exception.ProcessingException;
import org.mja.account.util.HtmlUtil;

public abstract class AbstractEndpoint implements Endpoint {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private String getEndpointDescription() {
    return getMethod().name() + " " + getRequestPath();
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    var requestJson = extractRequest(httpExchange);

    EndpointResponse response;
    try {
      response = process(requestJson);
    } catch (ProcessingException e) {
      logger.info("exception executing " + getEndpointDescription() + ": " + e.getMessage());
      response = e.toResponse();
    }

    HtmlUtil.sendResponse(httpExchange, response);
  }

  private EndpointRequest extractRequest(HttpExchange httpExchange) throws IOException {
    Json json;
    if (getMethod().isSupportsJsonInRequest()) {
      try (var requestInputStream = httpExchange.getRequestBody()) {
        var requestAsString = new String(requestInputStream.readAllBytes());
        json = Json.read(requestAsString);
      }
    } else {
      json = null;
    }

    return EndpointRequest.builder().json(json)
        .uri(httpExchange.getRequestURI()).build();
  }

  protected abstract EndpointResponse process(EndpointRequest request);

}
