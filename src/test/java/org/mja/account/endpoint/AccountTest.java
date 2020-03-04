package org.mja.account.endpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.http.HttpResponse;
import mjson.Json;
import org.junit.After;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;

public class AccountTest extends BaseIntegrationTest {

  @Test
  public void createAndGetAnAccount() throws IOException, InterruptedException {
    var body = "{\n"
        + "\"number\": \"10\",\n"
        + "\"currency\": \"PLN\",\n"
        + "\"balance\": 1000\n"
        + "}";
    var response = post("accounts", body);

    assertThat(response.statusCode(), is(200));

    var responseJson = Json.read(response.body());
    var id = (String) responseJson.at("id").getValue();
    var getResponse = get("accounts/%s",id);
    var getJson = Json.read(getResponse.body());

    assertThat(getJson.at("id").getValue(), is(id));
    assertThat(getJson.at("number").getValue(), is("10"));
    assertThat(getJson.at("currency").getValue(), is("PLN"));
    assertThat(getJson.at("balance").getValue(), is(1000L));
  }

  @Test
  public void createAnAccount() throws IOException, InterruptedException {
    var body = "{\n"
        + "\"number\": \"20\",\n"
        + "\"currency\": \"EUR\",\n"
        + "\"balance\": 2000\n"
        + "}";

    var path = "accounts";
    HttpResponse<String> response = post(path, body);

    assertThat(response.statusCode(), is(200));
    Json responseJson = Json.read(response.body());
    assertThat(responseJson.at("id"), notNullValue());
    assertThat(responseJson.at("number").getValue(), is("20"));
    assertThat(responseJson.at("currency").getValue(), is("EUR"));
    assertThat(responseJson.at("balance").getValue(), is(2000L));
  }

}