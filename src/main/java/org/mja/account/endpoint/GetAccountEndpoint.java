package org.mja.account.endpoint;

import java.util.logging.Logger;
import javax.inject.Inject;
import org.mja.account.endpoint.exception.NotFoundException;
import org.mja.account.http.HttpMethod;
import org.mja.account.model.AccountEntity;
import org.mja.account.repository.AccountRepository;

public class GetAccountEndpoint extends AbstractEndpoint {

  private static final String ACCOUNTS = "/accounts";
  private final AccountRepository accountRepository;
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  public GetAccountEndpoint(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  public String getRequestPath() {
    return ACCOUNTS;
  }

  @Override
  protected EndpointResponse process(EndpointRequest request) {
    String id = request.getLastPathParamValue();
    logger.info("get an account for id " + id);
    return EndpointResponse.fromJson(AccountEntity.toJson(accountRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("account with id " + id + " not found"))));
  }
}
