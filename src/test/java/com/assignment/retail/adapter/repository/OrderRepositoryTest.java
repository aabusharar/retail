package com.assignment.retail.adapter.repository;

import com.assignment.retail.adapter.TestConfig;
import com.assignment.retail.adapter.repository.entity.OrderEntity;
import com.assignment.retail.adapter.repository.jpa.dao.OrderRepositoryDao;
import com.assignment.retail.model.Order;
import com.assignment.retail.model.ProductDetails;
import com.assignment.retail.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

@DataJpaTest
@Import(TestConfig.class)
public class OrderRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private OrderRepositoryDao orderRepositoryDao;

  @Autowired
  private OrderRepository repository;

  @BeforeEach
  public void beforeTests() {
    orderRepositoryDao.deleteAll();

    String address = "Amman";
    String email = "xxx@gmail.com";
    String phoneNumber = "xxxxxxxxxxxx";
    OrderEntity fruit = OrderEntity.builder()
            .id(1)
            .userName("registeredUser")
            .orderTime(LocalDateTime.now())
            .address(address)
            .email(email)
            .phoneNumber(phoneNumber)
            .totalOrderPrice(20.0)
            .products(Collections.singletonList(ProductDetails.builder().name("Fruit").id(1).amount(2).build()))
            .build();

    OrderEntity clothes = OrderEntity.builder()
            .id(2)
            .userName("registeredUser")
            .orderTime(LocalDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(0, 0, 0, 0)))
            .address(address)
            .email(email)
            .phoneNumber(phoneNumber)
            .totalOrderPrice(50.0)
            .products(Collections.singletonList(ProductDetails.builder().name("Clothes").id(2).amount(3).build()))
            .build();

    orderRepositoryDao.save(fruit);
    orderRepositoryDao.save(clothes);
  }

  @Test
  public void testWhenUserNotOrderedBefore() {
    String userName = "notFoundUser";
    Optional<Order> order = repository.findOldestOrderByUserName(userName);
    Assertions.assertFalse(order.isPresent());
  }

  @Test
  public void testWhenUserOrderedBefore() {
    String userName = "registeredUser";
    LocalDateTime time = LocalDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(0, 0, 0, 0));
    Optional<Order> maybeOrder = repository.findOldestOrderByUserName(userName);
    Assertions.assertTrue(maybeOrder.isPresent());
    Order order = maybeOrder.get();
    Assertions.assertEquals(order.getOrderTime(), time);
    Assertions.assertEquals(order.getUserName(), "registeredUser");
    Assertions.assertEquals(order.getId(), 2);
  }
}
