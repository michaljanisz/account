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
import lombok.SneakyThrows;

@Singleton
public class AccountHttpServer implements Runnable {

  private Collection<Filter> filters;
  private HttpHandler httpHandler;
  private int port = 8080;
  private HttpServer server;

  @Inject
  @SneakyThrows
  public AccountHttpServer(Collection<Filter> filters,
      HttpHandler httpHandler) {
    this.filters = filters;
    this.httpHandler = httpHandler;
    this.server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
  }

  @SneakyThrows
  public void startInANewThread(int port) throws IOException {
    new Thread(this).start();
    var logger = Logger.getLogger(AccountHttpServer.class.getName());
    logger.info("  on port " + port);
  }

  @SneakyThrows
  @Override
  public void run() {
    var logger = Logger.getLogger(AccountHttpServer.class.getName());
    var threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    server.createContext("/", httpHandler).getFilters()
        .addAll(filters);
    server.setExecutor(threadPoolExecutor);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Server is shutting down...");
      server.stop(1);
      logger.info("Server stopped");
    }));
    logger.info("starting server...");
    server.start();
    logger.info(" Server started on port " + port);
  }

  public void stop() {
    server.stop(0);
  }
}
