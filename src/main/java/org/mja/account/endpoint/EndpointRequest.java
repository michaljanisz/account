package org.mja.account.endpoint;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import mjson.Json;
import org.mja.account.endpoint.exception.BadRequestException;

@Data
public class EndpointRequest {

  private final HashMap<String, String> params;
  private final Json json;
  private String uriAsString;
  private String path;

  @Builder
  public EndpointRequest(Json json, URI uri) {
    this.json = json;
    this.params = new HashMap<>();
    if (uri != null) {
      this.uriAsString = uri.toString();
      if (uriAsString.contains("?")) {
        var items =
            uriAsString.split("\\?")[1]
                .split("=");
        path = items[0];
        for (int i = 0; i < items.length; i += 2) {
          params.put(items[i], items[i + 1]);
        }
      } else {
        path = uriAsString;
      }
    }

  }

  public String getQueryParamValueOrThrow(String name) {
    return Optional.ofNullable(params.get(name))
        .orElseThrow(() -> new BadRequestException("missing parameter " + name));
  }

  public String getLastPathParamValue() {
    int idx = uriAsString.lastIndexOf('/');
    return path.substring(idx + 1);
  }
}