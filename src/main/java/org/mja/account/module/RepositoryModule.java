package org.mja.account.module;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.mja.account.repository.AccountRepository;
import org.mja.account.repository.TransferRepository;

@Module
public class RepositoryModule {

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
