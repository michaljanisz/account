package org.mja.account.module;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Singleton;
import org.mja.account.endpoint.CreateAccountEndpoint;
import org.mja.account.endpoint.Endpoint;

@Module
public class EndpointsModule {

  @Provides
  @Singleton
  @IntoSet
  Endpoint providePostTransferEndpoint() {
    return new CreateAccountEndpoint();
  }
}
