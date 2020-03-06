package org.mja.account.endpoint;

import static org.junit.Assert.assertThat;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import java.util.stream.IntStream;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mja.account.BaseIntegrationTest;
import org.mja.account.model.Account;

@RunWith(ConcurrentTestRunner.class)
public class TransferRaceConditionTest extends BaseIntegrationTest {

  private static final int INITIAL_BALANCE = 1_000_000;
  private static final int THREAD_COUNT = 10;
  private static final int NO_OF_TRANSFERS_PER_THREAD = 100;
  private static final int TRANSFER_AMOUNT = 10;

  private Account fromAccount;
  private Account toAccount;

  @Before
  public void initializeAccounts() {
    this.fromAccount = createAccount(INITIAL_BALANCE);
    this.toAccount = createAccount(INITIAL_BALANCE);
  }

  @Test
  @ThreadCount(THREAD_COUNT)
  public void testTransferForRaceCondition() {
    IntStream.range(0, NO_OF_TRANSFERS_PER_THREAD).forEach(i -> {
      createTransfer(fromAccount, toAccount, TRANSFER_AMOUNT);
    });
  }

  @After
  public void assertBalance() {
    var diff = THREAD_COUNT * NO_OF_TRANSFERS_PER_THREAD * TRANSFER_AMOUNT;

    var fromAccount = getAccount(this.fromAccount.getId());
    var toAccount = getAccount(this.toAccount.getId());

    assertThat(fromAccount.getBalance().intValue(), CoreMatchers.is(INITIAL_BALANCE - diff));
    assertThat(toAccount.getBalance().intValue(), CoreMatchers.is(INITIAL_BALANCE + diff));
  }

}