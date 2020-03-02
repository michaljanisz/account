package org.mja.account.module;

import dagger.Component;
import javax.inject.Singleton;
import org.mja.account.repository.AccountRepository;
import org.mja.account.repository.TransferRepository;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface RepoBuilder {

  AccountRepository accountRepository();

  TransferRepository transferRepository();
}