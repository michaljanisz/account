package org.mja.account.module;

import com.sun.net.httpserver.Filter;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.util.Set;
import javax.inject.Singleton;
import org.mja.account.endpoint.Endpoint;
import org.mja.account.http.AccountHttpServer;
import org.mja.account.http.AccountHttpServerLoggingFilter;
import org.mja.account.http.EndpointHandler;

@Module(includes = {EndpointsModule.class, RepositoryModule.class})
public class AccountHttpServerModule {

  @Provides
  @IntoSet
  @Singleton
  Filter provideAccountHttpServerLoggingFilter() {
    return new AccountHttpServerLoggingFilter();
  }

  @Provides
  @Singleton
  AccountHttpServer provideAccountHttpServer(Set<Filter> filter, EndpointHandler myHttpHandler) {
    return new AccountHttpServer(filter, myHttpHandler);
  }

  @Provides
  @Singleton
  EndpointHandler provideEndpointHandler(Set<Endpoint> endpoints) {
    return new EndpointHandler(endpoints);
  }


}
