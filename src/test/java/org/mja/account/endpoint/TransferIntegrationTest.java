package org.mja.account.endpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;
import org.mja.account.model.TransferEntity;

public class TransferIntegrationTest extends BaseIntegrationTest {

  @Test
  public void doTransfer() {
    var fromAccount = createAccount(1000);
    var toAccount = createAccount(1000);

    var created = createTransfer(fromAccount, toAccount, 200);

    assertThat(created.getId(), notNullValue());
    assertThat(created.getFromAccountId(), is(fromAccount.getId()));
    assertThat(created.getToAccountId(), is(toAccount.getId()));
    assertThat(created.getAmount(), is(BigDecimal.valueOf(200)));

    var fromBalance = getAccount(fromAccount.getId()).getBalance();
    var toBalance = getAccount(toAccount.getId()).getBalance();

    assertThat(fromBalance, is(BigDecimal.valueOf(800)));
    assertThat(toBalance, is(BigDecimal.valueOf(1200)));
  }

  @Test
  public void shouldFailToCreateTransfer_whenAmountBelowZero() {
    var fromAccount = createAccount(1000);
    var toAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(-100))
        .build();

    assertTransferResponse(transfer, 422, "amount must be greater then 0");
  }

  @Test
  public void shouldFailToCreateTransfer_whenAmountIsEmpty() {
    var fromAccount = createAccount(1000);
    var toAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(null)
        .build();

    assertTransferResponse(transfer, 422, "amount cannot be null");
  }

  @Test
  public void shouldFailToCreateTransfer_whenFromAccountEmpty() {
    var toAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(null)
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(100))
        .build();

    assertTransferResponse(transfer, 422, "from account cannot be null");
  }

  @Test
  public void shouldFailToCreateTransfer_whenToAccountEmpty() {
    var fromAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(null)
        .amount(BigDecimal.valueOf(100))
        .build();

    assertTransferResponse(transfer, 422, "to account cannot be null");
  }

  @Test
  public void shouldFailToCreateTransfer_whenToAccountInDifferentCurrencies() {
    var fromAccount = createAccount(1000, "EUR");
    var toAccount = createAccount(1000, "USD");
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(100))
        .build();

    assertTransferResponse(transfer, 422, "cannot do transfer between two different currencies");
  }

  @Test
  public void shouldFailToCreateTransfer_whenBalanceBelow0() {
    var fromAccount = createAccount(1000);
    var toAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(2000))
        .build();

    assertTransferResponse(transfer, 422,
        "Cannot go below zero for account " + fromAccount.getId());
  }

  @Test
  public void shouldFailToCreateTransfer_whenFromAccountNotFound() {
    var toAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId("not_existing_id")
        .toAccountId(toAccount.getId())
        .amount(BigDecimal.valueOf(2000))
        .build();

    assertTransferResponse(transfer, 404,
        "account with id not_existing_id does not exist");
  }

  @Test
  public void shouldFailToCreateTransfer_whenToAccountNotFound() {
    var fromAccount = createAccount(1000);
    var transfer = TransferEntity.builder()
        .fromAccountId(fromAccount.getId())
        .toAccountId("not_existing_id")
        .amount(BigDecimal.valueOf(2000))
        .build();

    assertTransferResponse(transfer, 404,
        "account with id not_existing_id does not exist");
  }

  public void assertTransferResponse(TransferEntity transfer,
      int code,
      String message) {
    var response = post("transfers", transfer.toString());

    assertThat(response.statusCode(), is(code));
    assertThat(response.body(), is(message));
  }
}