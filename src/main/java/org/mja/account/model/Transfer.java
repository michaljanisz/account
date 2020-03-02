package org.mja.account.model;

import static org.mja.account.util.JsonUtil.asString;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mjson.Json;
import org.mja.account.endpoint.exception.UnprocessableEntityException;
import org.mja.account.util.JsonUtil;

@Getter
@Setter
public class Transfer extends BaseEntity {

  private final String fromAccountId;
  private final String toAccountId;
  private final BigDecimal amount;

  @Builder
  public Transfer(String id, String fromAccountId, String toAccountId, BigDecimal amount) {
    super(id);
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.amount = amount;
  }

  public static Transfer fromJson(Json json) {
    return Transfer.builder()
        .id(asString(json, "id"))
        .fromAccountId(asString(json, "from_account_id"))
        .toAccountId(asString(json, "to_account_id"))
        .amount(JsonUtil.getBigDecimalValue(json, "amount"))
        .build();

  }

  public static Json toJson(Transfer transfer) {
    return Json.object()
        .set("id", transfer.getId())
        .set("from_account_id", transfer.getFromAccountId())
        .set("to_account_id", transfer.getToAccountId())
        .set("amount", transfer.getAmount());
  }

  public void validateBeforeCreate() {
    if (fromAccountId == null) {
      throw new UnprocessableEntityException("from account cannot be null");
    }
    if (toAccountId == null) {
      throw new UnprocessableEntityException("to account cannot be null");
    }
    if (amount == null) {
      throw new UnprocessableEntityException("to amount cannot be null");
    }
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new UnprocessableEntityException("amount must be greater then 0");
    }
  }
}
