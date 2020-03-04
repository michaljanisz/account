package org.mja.account.endpoint;

import java.util.logging.Logger;
import javax.inject.Inject;
import org.mja.account.http.HttpMethod;
import org.mja.account.model.Account;
import org.mja.account.repository.AccountRepository;

public class CreateAccountEndpoint extends AbstractEndpoint {

  private static final String ACCOUNTS = "/accounts";
  private final AccountRepository accountRepository;
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  public CreateAccountEndpoint(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  @Override
  public String getRequestPath() {
    return ACCOUNTS;
  }

  @Override
  protected EndpointResponse process(EndpointRequest request) {
    Account account = Account.fromJson(request.getJson());
    logger.info("creating an account " + account);
    return EndpointResponse.fromJson(Account.toJson(accountRepository.create(account)));
  }
}
