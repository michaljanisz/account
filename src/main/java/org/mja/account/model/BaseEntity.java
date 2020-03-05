package org.mja.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.mja.account.endpoint.exception.BadRequestException;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public abstract class BaseEntity {

  private String id;

  public abstract void validateBeforeCreate() throws BadRequestException;
}
