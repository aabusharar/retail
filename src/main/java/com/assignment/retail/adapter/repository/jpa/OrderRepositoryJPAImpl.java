package com.assignment.retail.adapter.repository.jpa;

import com.assignment.retail.adapter.repository.OrderRepository;
import com.assignment.retail.adapter.repository.entity.OrderEntity;
import com.assignment.retail.adapter.repository.jpa.dao.OrderRepositoryDao;
import com.assignment.retail.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryJPAImpl implements OrderRepository {
  private final OrderRepositoryDao repo;

  @Override
  public Optional<Order> findOldestOrderByUserName(String userName) {
    Pageable limit = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "orderTime"));
    return repo.findAllByUserNameIgnoreCase(userName, limit)
            .stream()
            .findFirst()
            .map(this::toModel);
  }

  private Order toModel(OrderEntity entity) {
    return Optional.ofNullable(entity)
            .map(e -> Order.builder()
                    .userName(e.getUserName())
                    .email(e.getEmail())
                    .address(e.getAddress())
                    .id(e.getId())
                    .phoneNumber(e.getPhoneNumber())
                    .orderTime(e.getOrderTime())
                    .totalOrderPrice(e.getTotalOrderPrice())
                    .products(e.getProducts())
                    .build())
            .orElseGet(() -> null);
  }
}
