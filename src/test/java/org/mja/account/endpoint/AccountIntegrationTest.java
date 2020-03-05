package org.mja.account.endpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import mjson.Json;
import org.junit.After;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;
import org.mja.account.model.Account;

public class AccountIntegrationTest extends BaseIntegrationTest {

  @Test
  public void createAndGetAnAccount() throws IOException, InterruptedException {
    var account = Account.builder()
        .number("10")
        .currency("PLN")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var created = createAccount(account);


    assertThat(created.getId(), notNullValue());
    assertThat(created.getNumber(), is("10"));
    assertThat(created.getCurrency(), is("PLN"));
    assertThat(created.getBalance(), is(BigDecimal.valueOf(1000)));

    var getResponse = get("accounts/%s",created.getId());
    var getJson = Json.read(getResponse.body());
    var fetchedAccount = Account.fromJson(getJson);

    assertThat(fetchedAccount.getId(), is(created.getId()));
    assertThat(fetchedAccount.getNumber(), is("10"));
    assertThat(fetchedAccount.getCurrency(), is("PLN"));
    assertThat(fetchedAccount.getBalance(), is(BigDecimal.valueOf(1000)));
  }

  @Test
  public void createAnAccount() throws IOException, InterruptedException {
    var account = Account.builder()
        .number("20")
        .currency("EUR")
        .balance(BigDecimal.valueOf(2000))
        .build();

    var created = createAccount(account);

    assertThat(created.getId(), notNullValue());
    assertThat(created.getNumber(), is("20"));
    assertThat(created.getCurrency(), is("EUR"));
    assertThat(created.getBalance(), is(BigDecimal.valueOf(2000)));
  }

  // should not create an account for bad request

  @Test
  public void shouldNotCreateAnAccount_whenNoCurrency() throws IOException, InterruptedException {
    var account = Account.builder()
        .number("10")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var response = createAccountReturnResponse(account);
    assertThat(response.statusCode(), is(400));
  }

  @Test
  public void shouldNotCreateAnAccount_whenNoNumber() throws IOException, InterruptedException {
    var account = Account.builder()
        .currency("PLN")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var response = createAccountReturnResponse(account);
    assertThat(response.statusCode(), is(400));
  }
}