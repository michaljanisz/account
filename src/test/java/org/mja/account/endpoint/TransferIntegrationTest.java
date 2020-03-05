package org.mja.account.endpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import mjson.Json;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;
import org.mja.account.model.Transfer;

public class TransferIntegrationTest extends BaseIntegrationTest {

  @Test
  public void doTransfer() throws IOException, InterruptedException {
    var fromAccount = createAccount(1000);
    var toAccount = createAccount(1000);

    var transfer = Transfer.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(200))
        .build();
    var response = post("transfers", transfer.toString());

    assertThat(response.statusCode(), is(200));

    Transfer created = Transfer.fromJson(Json.read(response.body()));
    assertThat(created.getId(), notNullValue());
    assertThat(created.getFromAccountId(), is(fromAccount.getId()));
    assertThat(created.getToAccountId(), is(toAccount.getId()));
    assertThat(created.getAmount(), is(BigDecimal.valueOf(200)));

    var fromBalance = getAccount(fromAccount.getId()).getBalance();
    var toBalance = getAccount(toAccount.getId()).getBalance();

    assertThat(fromBalance, is(BigDecimal.valueOf(800)));
    assertThat(toBalance, is(BigDecimal.valueOf(1200)));

  }


}