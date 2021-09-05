package com.assignment.retail.adapter.repository;

import com.assignment.retail.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
  Optional<Order> findOldestOrderByUserName(String userName);
}
