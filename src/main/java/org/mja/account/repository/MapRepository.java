package org.mja.account.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.mja.account.model.BaseEntity;

public abstract class MapRepository<T extends BaseEntity> implements Repository<T> {

  protected final Map<String, T> storageMap = new HashMap<>();

  @Override
  public T create(T obj) {
    obj.setId(UUID.randomUUID().toString());
    storageMap.put(obj.getId(), obj);
    return obj;
  }

  public Optional<T> findById(String id) {
    return Optional.ofNullable(storageMap.get(id));
  }

}
