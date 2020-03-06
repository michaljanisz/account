package org.mja.account.module;

import dagger.Component;
import javax.inject.Singleton;
import org.mja.account.http.AccountHttpServer;

@Singleton
@Component(modules = {AccountHttpServerModule.class})
public interface ServerBuilder {
  AccountHttpServer server();
}