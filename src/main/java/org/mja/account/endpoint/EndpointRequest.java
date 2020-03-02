package org.mja.account.endpoint;

import lombok.Data;
import lombok.experimental.Accessors;
import mjson.Json;

@Data
@Accessors(chain = true)
public class EndpointRequest {

  public static final EndpointRequest EMPTY = new EndpointRequest();
  private Json json;
}