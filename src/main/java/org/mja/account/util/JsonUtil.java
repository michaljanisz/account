package org.mja.account.util;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import mjson.Json;

@UtilityClass
public class JsonUtil {

  public static BigDecimal getBigDecimalValue(Json json, String fieldName) {
    final Json at = json.at(fieldName);
    if (at == null) {
      return null;
    }
    Object balance = json.at(fieldName).getValue();
    BigDecimal balBig = null;
    if (balance instanceof Long) {
      balBig = BigDecimal.valueOf((Long) balance);
    } else if (balance instanceof Double) {
      balBig = BigDecimal.valueOf((Double) balance);
    } else if (balance instanceof BigDecimal) {
      balBig = (BigDecimal) balance;
    }
    return balBig;
  }

  public static String asString(Json json, String fieldName) {
    final Json at = json.at(fieldName);
    if (at == null) {
      return "";
    }
    return at.asString();
  }
}
