package org.mja.account.endpoint;

import java.io.IOException;
import org.junit.Test;
import org.mja.account.BaseIntegrationTest;

public class TransferIntegrationTest extends BaseIntegrationTest {


  @Test
  public void doTransfer() throws IOException, InterruptedException {
    var account1 = createAccount(1000);
    var account2 = createAccount(1000);


//    var response = post("accounts", body);
  }


}