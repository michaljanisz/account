package org.mja.account.endpoint;

import java.util.logging.Logger;
import org.mja.account.http.HttpMethod;
import org.mja.account.model.Account;
import org.mja.account.module.DaggerRepoBuilder;
import org.mja.account.module.RepoBuilder;
import org.mja.account.repository.AccountRepository;

public class CreateAccountEndpoint extends AbstractEndpoint {

  private static final String ACCOUNTS = "/accounts";
  private final AccountRepository accountRepository;
  private final Logger logger = Logger.getLogger(getClass().getName());

  public CreateAccountEndpoint() {
    final RepoBuilder repoBuilder = DaggerRepoBuilder.builder().build();
    accountRepository = repoBuilder.accountRepository();
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
