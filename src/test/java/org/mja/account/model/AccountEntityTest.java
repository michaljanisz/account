package org.mja.account.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import mjson.Json;
import org.junit.Test;

public class AccountEntityTest {

  @Test
  public void shouldParseJson() {
    // given
    var accountAsString = "{\"number\":\"10100\",\"balance\":10.12,\"currency\":\"EUR\",\"id\":\"7b0b2222-6a87-4068-b8a2-111038a6bc4d\"}";

    AccountEntity account = AccountEntity.fromJson(Json.read(accountAsString));

    assertThat(account.getId(), is("7b0b2222-6a87-4068-b8a2-111038a6bc4d"));
    assertThat(account.getNumber(), is("10100"));
    assertThat(account.getCurrency(), is("EUR"));
    assertThat(account.getBalance(), is(BigDecimal.valueOf(10.12)));
  }

  @Test
  public void shouldSerializeToJson() {
    // given
    var account = AccountEntity.builder()
        .id("7b0b2222-6a87-4068-b8a2-111038a6bc4d")
        .number("10100")
        .currency("EUR")
        .balance(BigDecimal.valueOf(10.12))
        .build();

    Json asJson = AccountEntity.toJson(account);
    AccountEntity accountFromJson = AccountEntity.fromJson(asJson);

    assertThat(accountFromJson.getId(), is("7b0b2222-6a87-4068-b8a2-111038a6bc4d"));
    assertThat(accountFromJson.getNumber(), is("10100"));
    assertThat(accountFromJson.getCurrency(), is("EUR"));
    assertThat(accountFromJson.getBalance(), is(BigDecimal.valueOf(10.12)));
  }


}