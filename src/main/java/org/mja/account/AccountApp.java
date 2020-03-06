package org.mja.account;


import org.mja.account.module.DaggerServerBuilder;
import org.mja.account.module.ServerBuilder;

public class AccountApp {

  public static void main(String[] args) {
    ServerBuilder serverBuilder = DaggerServerBuilder.builder().build();
    serverBuilder.server().run();
  }

}
