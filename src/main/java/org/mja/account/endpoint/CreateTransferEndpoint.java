package org.mja.account.endpoint;

import java.util.logging.Logger;
import javax.inject.Inject;
import org.mja.account.endpoint.exception.NotFoundException;
import org.mja.account.endpoint.exception.UnprocessableEntityException;
import org.mja.account.http.HttpMethod;
import org.mja.account.model.AccountEntity;
import org.mja.account.model.TransferEntity;
import org.mja.account.repository.AccountRepository;
import org.mja.account.repository.TransferRepository;

public class CreateTransferEndpoint extends AbstractEndpoint {

  private static final String TRANSFERS = "/transfers";
  private final Logger logger = Logger.getLogger(getClass().getName());

  private final AccountRepository accountRepository;
  private final TransferRepository transferRepository;

  @Inject
  public CreateTransferEndpoint(AccountRepository accountRepository,
      TransferRepository transferRepository) {
    this.accountRepository = accountRepository;
    this.transferRepository = transferRepository;
  }

  @Override
  protected EndpointResponse process(EndpointRequest request) {
    TransferEntity transfer = TransferEntity.fromJson(request.getJson());
    transfer.validateBeforeCreate();
    AccountEntity toAccount = checkAccountExists(transfer.getToAccountId());

    // this part should be synchronized
    synchronized (this) {
      AccountEntity fromAccount = checkAccountExists(transfer.getFromAccountId());

      validateCurrencies(toAccount, fromAccount);
      fromAccount.debitAmountIfPossible(transfer.getAmount());
      toAccount.creditAmount(transfer.getAmount());
      logger.info("creating a transfer " + transfer);
      return EndpointResponse.fromJson(TransferEntity.toJson(transferRepository.create(transfer)));
    }
  }

  private void validateCurrencies(AccountEntity toAccount, AccountEntity fromAccount) {
    if (fromAccount.getCurrency() != null && !fromAccount.getCurrency()
        .equals(toAccount.getCurrency())) {
      throw new UnprocessableEntityException("cannot do transfer between two different currencies");
    }
  }

  private AccountEntity checkAccountExists(String accountId) {
    return accountRepository.findById(accountId)
        .orElseThrow(
            () -> new NotFoundException("account with id " + accountId + " does not exist"));
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  public String getRequestPath() {
    return TRANSFERS;
  }

}
