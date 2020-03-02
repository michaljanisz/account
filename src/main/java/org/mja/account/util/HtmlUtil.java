package org.mja.account.util;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.mja.account.endpoint.EndpointResponse;
import org.mja.account.http.HttpStatus;

@UtilityClass
public class HtmlUtil {

  public void sendResponse(HttpExchange httpExchange, String htmlResponse, HttpStatus status)
      throws IOException {
    var outputStream = httpExchange.getResponseBody();
    httpExchange.sendResponseHeaders(status.getCode(), htmlResponse.length());
    outputStream.write(htmlResponse.getBytes());
    outputStream.flush();
    outputStream.close();
  }

  public static void sendResponse(HttpExchange httpExchange, EndpointResponse response)
      throws IOException {
    sendResponse(httpExchange, response.getResponseString(), response.getStatus());
  }
}
