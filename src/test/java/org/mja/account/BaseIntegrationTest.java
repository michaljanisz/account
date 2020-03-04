package org.mja.account;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import mjson.Json;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mja.account.http.AccountHttpServer;
import org.mja.account.module.DaggerServerBuilder;
import org.mja.account.module.ServerBuilder;

public abstract class BaseIntegrationTest {

  static final String BASE_URL = "http://localhost:8080/";
  private static AccountHttpServer server;

  private final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .build();

  @Before
  public void setUp() throws Exception {
    makeSureServerStarted();

  }

  private static synchronized void makeSureServerStarted() throws IOException {
    if (server == null) {
      ServerBuilder serverBuilder = DaggerServerBuilder.builder().build();
      server = serverBuilder.server();
      server.startInANewThread(8080);
    }
  }

  protected HttpResponse<String> post(String path, String body)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create("http://localhost:8080/" + path))
        .header("Content-Type", "application/json")
        .build();

    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  protected HttpResponse<String> get(String path, String ... params)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(BASE_URL + String.format(path, params)))
        .header("Content-Type", "application/json")
        .build();

    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

 // @AfterClass
  //public void tearDown() throws Exception {
   // server.stop();
 // }
}