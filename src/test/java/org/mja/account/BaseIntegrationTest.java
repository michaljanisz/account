package org.mja.account;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import mjson.Json;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.mja.account.http.AccountHttpServer;
import org.mja.account.model.Account;
import org.mja.account.model.Transfer;
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

  protected HttpResponse<String> post(String path, String body) {
    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create("http://localhost:8080/" + path))
        .header("Content-Type", "application/json")
        .build();

    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  protected HttpResponse<String> get(String path, String... params) {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(BASE_URL + String.format(path, (Object[]) params)))
        .header("Content-Type", "application/json")
        .build();

    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  protected Account createAccount(int balance) {
    return createAccount(balance, "EUR");
  }

  protected Account createAccount(int balance, String currency) {
    return createAccount(
        Account.builder()
            .number(UUID.randomUUID().toString())
            .balance(new BigDecimal((balance)))
            .currency(currency).build());
  }

  protected HttpResponse<String> createAccountReturnResponse(Account input) {
    Json accountAsJson = Account.toJson(input);
    var response = post("accounts", accountAsJson.toString());

    return response;
  }

  protected Account createAccount(Account input) {
    HttpResponse<String> response = createAccountReturnResponse(input);
    assertThat(response.statusCode(), is(200));

    var responseJson = Json.read(response.body());
    var account = Account.fromJson(responseJson);
    return account;
  }

  protected Account getAccount(String id) {
    var getResponse = get("accounts/%s", id);
    assertThat(getResponse.statusCode(), is(200));
    var getJson = Json.read(getResponse.body());
    return Account.fromJson(getJson);
  }

  protected Transfer createTransfer(Account fromAccount, Account toAccount, int amount) {
    var transfer = Transfer.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(amount))
        .build();
    var response = post("transfers", transfer.toString());

    MatcherAssert.assertThat(response.statusCode(), is(200));
    Transfer created = Transfer.fromJson(Json.read(response.body()));
    return created;
  }
  // @AfterClass
  //public void tearDown() throws Exception {
  // server.stop();
  // }
}