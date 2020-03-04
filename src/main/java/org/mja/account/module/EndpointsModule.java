package org.mja.account.module;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.mja.account.endpoint.CreateAccountEndpoint;
import org.mja.account.endpoint.CreateTransferEndpoint;
import org.mja.account.endpoint.Endpoint;
import org.mja.account.endpoint.GetAccountEndpoint;
import org.mja.account.repository.AccountRepository;
import org.mja.account.repository.TransferRepository;

@Module
public class EndpointsModule {

  @Provides
  @Singleton
  @IntoSet
  @Inject
  Endpoint providePostTransferEndpoint(AccountRepository accountRepository) {
    return new CreateAccountEndpoint(accountRepository);
  }

  @Provides
  @Singleton
  @IntoSet
  @Inject
  Endpoint provideCreateTransferEndpoint(AccountRepository accountRepository,
      TransferRepository transferRepository) {
    return new CreateTransferEndpoint(accountRepository, transferRepository);
  }

  @Provides
  @Singleton
  @IntoSet
  @Inject
  Endpoint provideGetAccountEndpoint(AccountRepository accountRepository) {
    return new GetAccountEndpoint(accountRepository);
  }


  @Provides
  @Singleton
  AccountRepository provideAccountRepository() {
    return new AccountRepository();
  }

  @Provides
  @Singleton
  TransferRepository provideTransferRepository() {
    return new TransferRepository();
  }
}
