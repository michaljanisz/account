package org.mja.account.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import mjson.Json;
import org.junit.Test;

public class TransferEntityTest {

  @Test
  public void shouldParseJson() {
    // given
    var transferAsTest = "{\"amount\":999.99,\"to_account_id\":\"c37dcd36-4791-4319-b9c1-69d899ce7051\",\"id\":\"59cc083a-5267-4618-ac27-b241759f3555\",\"from_account_id\":\"e70997a2-9733-4f87-8902-b3e3b2307d79\"}";

    // when
    final TransferEntity transfer = TransferEntity.fromJson(Json.read(transferAsTest));

    // then
    assertThat(transfer.getId(), is("59cc083a-5267-4618-ac27-b241759f3555"));
    assertThat(transfer.getFromAccountId(), is("e70997a2-9733-4f87-8902-b3e3b2307d79"));
    assertThat(transfer.getToAccountId(), is("c37dcd36-4791-4319-b9c1-69d899ce7051"));
    assertThat(transfer.getAmount(), is(BigDecimal.valueOf(999.99)));
  }

  @Test
  public void shouldSerializeToJson() {
    // given
    var id = UUID.randomUUID().toString();
    var fromId = UUID.randomUUID().toString();
    var toId = UUID.randomUUID().toString();
    var amount = BigDecimal.valueOf(999.99);
    var transfer = TransferEntity.builder()
        .id(id)
        .fromAccountId(fromId)
        .toAccountId(toId)
        .amount(amount)
        .build();
    //when
    Json asJson = TransferEntity.toJson(transfer);
    TransferEntity transferFromJson = TransferEntity.fromJson(asJson);

    //then
    assertThat(transferFromJson.getId(), is(id));
    assertThat(transferFromJson.getFromAccountId(), is(fromId));
    assertThat(transferFromJson.getToAccountId(), is(toId));
    assertThat(transferFromJson.getAmount(), is(amount));
  }


}