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
import org.mja.account.model.AccountEntity;
import org.mja.account.model.TransferEntity;
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

  protected AccountEntity createAccount(int balance) {
    return createAccount(balance, "EUR");
  }

  protected AccountEntity createAccount(int balance, String currency) {
    return createAccount(
        AccountEntity.builder()
            .number(UUID.randomUUID().toString())
            .balance(new BigDecimal((balance)))
            .currency(currency).build());
  }

  protected HttpResponse<String> createAccountReturnResponse(AccountEntity input) {
    Json accountAsJson = AccountEntity.toJson(input);
    var response = post("accounts", accountAsJson.toString());

    return response;
  }

  protected AccountEntity createAccount(AccountEntity input) {
    HttpResponse<String> response = createAccountReturnResponse(input);
    assertThat(response.statusCode(), is(200));

    var responseJson = Json.read(response.body());
    var account = AccountEntity.fromJson(responseJson);
    return account;
  }

  protected AccountEntity getAccount(String id) {
    var getResponse = get("accounts/%s", id);
    assertThat(getResponse.statusCode(), is(200));
    var getJson = Json.read(getResponse.body());
    return AccountEntity.fromJson(getJson);
  }

  protected TransferEntity createTransfer(AccountEntity fromAccount, AccountEntity toAccount,
      int amount) {
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(amount))
        .build();
    var response = post("transfers", transfer.toString());

    MatcherAssert.assertThat(response.statusCode(), is(200));
    TransferEntity created = TransferEntity.fromJson(Json.read(response.body()));
    return created;
  }
  // @AfterClass
  //public void tearDown() throws Exception {
  // server.stop();
  // }
}