package com.assignment.retail.adapter;

import com.assignment.retail.adapter.repository.OrderRepository;
import com.assignment.retail.adapter.repository.ProductRepository;
import com.assignment.retail.adapter.repository.UserRepository;
import com.assignment.retail.adapter.repository.jpa.OrderRepositoryJPAImpl;
import com.assignment.retail.adapter.repository.jpa.ProductRepositoryJPAImpl;
import com.assignment.retail.adapter.repository.jpa.UserRepositoryJPAImpl;
import com.assignment.retail.adapter.repository.jpa.dao.OrderRepositoryDao;
import com.assignment.retail.adapter.repository.jpa.dao.ProductRepositoryDao;
import com.assignment.retail.adapter.repository.jpa.dao.UserRepositoryDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public ProductRepository productRepository(ProductRepositoryDao productRepositoryDao) {
    return new ProductRepositoryJPAImpl(productRepositoryDao);
  }

  @Bean
  public UserRepository userRepository(UserRepositoryDao userRepositoryDao) {
    return new UserRepositoryJPAImpl(userRepositoryDao);
  }

  @Bean
  public OrderRepository orderRepository(OrderRepositoryDao orderRepositoryDao) {
    return new OrderRepositoryJPAImpl(orderRepositoryDao);
  }
}
