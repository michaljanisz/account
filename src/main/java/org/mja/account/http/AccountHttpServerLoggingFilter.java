package org.mja.account.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountHttpServerLoggingFilter extends Filter {

  private final Logger logger = Logger.getLogger("http server");

  @Override
  public void doFilter(HttpExchange http, Chain chain) throws IOException {
    try {
      chain.doFilter(http);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "unexpected error", e);
    } finally {
      logger.info(String.format("%s %s %s",
          http.getRequestMethod(),
          http.getRequestURI().getPath(),
          http.getRemoteAddress()));
    }
  }

  @Override
  public String description() {
    return "logging";
  }
}
