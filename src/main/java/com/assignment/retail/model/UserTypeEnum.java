package com.assignment.retail.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserTypeEnum {
  EMPLOYEE(0.30), AFFILIATE(0.10), ANONYMOUS(0.0);

  private final double percentage;
}
