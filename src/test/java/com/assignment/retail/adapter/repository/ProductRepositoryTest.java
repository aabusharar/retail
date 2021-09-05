package com.assignment.retail.adapter.repository;

import com.assignment.retail.adapter.TestConfig;
import com.assignment.retail.adapter.repository.entity.ProductEntity;
import com.assignment.retail.adapter.repository.jpa.dao.ProductRepositoryDao;
import com.assignment.retail.model.Product;
import com.assignment.retail.model.ProductTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
public class ProductRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProductRepositoryDao productRepositoryDao;

  @Autowired
  private ProductRepository repository;

  @BeforeEach
  public void beforeTests() {
    productRepositoryDao.deleteAll();

    ProductEntity fruit = ProductEntity.builder()
            .id(1)
            .productType(ProductTypeEnum.GROCERIES)
            .price(10.0)
            .name("Fruit")
            .build();

    ProductEntity clothes = ProductEntity.builder()
            .id(2)
            .productType(ProductTypeEnum.SOFT_GOODS)
            .price(15.0)
            .name("Clothes")
            .build();

    productRepositoryDao.save(fruit);
    productRepositoryDao.save(clothes);
  }

  @Test
  @DisplayName("Test when Product in DB")
  public void testWhenAllProductFound() {
    List<Integer> productIds = Collections.singletonList(1);
    List<Product> products = repository.findAllByIds(productIds);
    Assertions.assertFalse(products.isEmpty());
    Assertions.assertEquals(1, products.size());
    Product product = products.get(0);
    Assertions.assertEquals(product.getId(), 1);
    Assertions.assertEquals(product.getProductType(), ProductTypeEnum.GROCERIES);
    Assertions.assertEquals(product.getPrice(), 10.0);
    Assertions.assertEquals(product.getName(), "Fruit");
  }

  @Test
  @DisplayName("Test Missing Product in DB")
  public void testWhenMissingProductFound() {
    List<Integer> productIds = Arrays.asList(2, 3);
    List<Product> products = repository.findAllByIds(productIds);
    Assertions.assertFalse(products.isEmpty());
    Assertions.assertEquals(1, products.size());
    Product product = products.get(0);
    Assertions.assertEquals(product.getId(), 2);
    Assertions.assertEquals(product.getProductType(), ProductTypeEnum.SOFT_GOODS);
    Assertions.assertEquals(product.getPrice(), 15.0);
    Assertions.assertEquals(product.getName(), "Clothes");
  }
}

