package org.mja.account.endpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;
import org.mja.account.model.AccountEntity;

public class AccountIntegrationTest extends BaseIntegrationTest {

  @Test
  public void createAndGetAnAccount() {
    var account = AccountEntity.builder()
        .number("10")
        .currency("PLN")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var created = createAccount(account);

    final String id = created.getId();
    assertThat(id, notNullValue());
    assertThat(created.getNumber(), is("10"));
    assertThat(created.getCurrency(), is("PLN"));
    assertThat(created.getBalance(), is(BigDecimal.valueOf(1000)));

    AccountEntity fetchedAccount = getAccount(id);

    assertThat(fetchedAccount.getId(), is(id));
    assertThat(fetchedAccount.getNumber(), is("10"));
    assertThat(fetchedAccount.getCurrency(), is("PLN"));
    assertThat(fetchedAccount.getBalance(), is(BigDecimal.valueOf(1000)));
  }

  @Test
  public void createAnAccount() {
    var account = AccountEntity.builder()
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

  @Test
  public void shouldNotCreateAnAccount_whenNoCurrency() {
    var account = AccountEntity.builder()
        .number("10")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var response = createAccountReturnResponse(account);
    assertThat(response.statusCode(), is(400));
  }

  @Test
  public void shouldNotCreateAnAccount_whenNoNumber() {
    var account = AccountEntity.builder()
        .currency("PLN")
        .balance(BigDecimal.valueOf(1000))
        .build();

    var response = createAccountReturnResponse(account);
    assertThat(response.statusCode(), is(400));
  }
}