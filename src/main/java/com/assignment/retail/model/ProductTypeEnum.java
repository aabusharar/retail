package com.assignment.retail.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ProductTypeEnum {
  GROCERIES(false), SOFT_GOODS(true);

  private final boolean isDiscountApplicable;
}
