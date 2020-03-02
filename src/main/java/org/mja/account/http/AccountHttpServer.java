package org.mja.account.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountHttpServer {

  private Collection<Filter> filters;
  private HttpHandler httpHandler;

  @Inject
  public AccountHttpServer(Collection<Filter> filters,
      HttpHandler httpHandler) {
    this.filters = filters;
    this.httpHandler = httpHandler;
  }

  public void start(int port) throws IOException {
    var logger = Logger.getLogger(AccountHttpServer.class.getName());
    var threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    var server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
    server.createContext("/", httpHandler).getFilters()
        .addAll(filters);
    server.setExecutor(threadPoolExecutor);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Server is shutting down...");
      server.stop(1);
      logger.info("Server stopped");
    }));
    server.start();

    logger.info(" Server started on port " + port);
  }

}
