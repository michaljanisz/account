package org.mja.account.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mjson.Json;
import org.mja.account.endpoint.exception.UnprocessableEntityException;
import org.mja.account.util.JsonUtil;


@Getter
@Setter
@ToString(callSuper = true)
public class Account extends BaseEntity {

  private final String number;
  private final String currency;
  private BigDecimal balance;

  @Builder
  public Account(String id, String number, String currency, BigDecimal balance) {
    super(id);
    this.number = number;
    this.currency = currency;
    if (balance == null) {
      this.balance = BigDecimal.ZERO;
    } else {
      this.balance = balance;
    }
  }

  public static Account fromJson(Json json) {
    return Account.builder()
        .id(JsonUtil.asString(json, "id"))
        .number(JsonUtil.asString(json, "number"))
        .currency(JsonUtil.asString(json, "currency"))
        .balance(JsonUtil.getBigDecimalValue(json, "balance"))
        .build();
  }

  public static Json toJson(Account account) {
    return Json.object()
        .set("id", account.getId())
        .set("number", account.getNumber())
        .set("currency", account.getCurrency())
        .set("balance", account.getBalance());
  }

  public void debitAmountIfPossible(BigDecimal amount) {
    validateDebit(amount);
    final BigDecimal balanceAfterOperation = balance.subtract(amount);
    if (balanceAfterOperation.compareTo(BigDecimal.ZERO) < 0) {
      throw new UnprocessableEntityException("Cannot go below zero for account " + getId());
    }
    setBalance(balanceAfterOperation);
  }


  private void validateDebit(BigDecimal amount) {
    if (amount == null) {
      throw new UnprocessableEntityException("cannot debit with empty amount");
    }
    if (amount == null) {
      throw new UnprocessableEntityException("cannot debit with empty amount");
    }
    if (amount == null) {
      throw new UnprocessableEntityException("amount cannot be null");
    }
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new UnprocessableEntityException("amount must be greater then 0");
    }

  }
}
