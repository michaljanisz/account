package org.mja.account.repository;

public interface Repository<T> {

  T create(T obj);
}
