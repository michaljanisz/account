package org.mja.account.endpoint;

import lombok.Builder;
import lombok.Data;
import mjson.Json;
import org.mja.account.http.HttpStatus;

@Data
@Builder
public class EndpointResponse {

  private String responseString;
  private HttpStatus status;

  public static EndpointResponse fromJson(Json json) {
    return EndpointResponse.builder()
        .status(HttpStatus.OK)
        .responseString(json.toString())
        .build();
  }
}