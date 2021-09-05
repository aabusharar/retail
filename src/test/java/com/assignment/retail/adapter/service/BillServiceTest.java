package com.assignment.retail.adapter.service;

import com.assignment.retail.adapter.repository.OrderRepository;
import com.assignment.retail.adapter.repository.ProductRepository;
import com.assignment.retail.adapter.repository.UserRepository;
import com.assignment.retail.adapter.rest.dto.BillDetailsDto;
import com.assignment.retail.adapter.rest.dto.ProductDto;
import com.assignment.retail.exception.ElementNotFoundException;
import com.assignment.retail.model.NetPayablePayment;
import com.assignment.retail.model.Order;
import com.assignment.retail.model.Product;
import com.assignment.retail.model.ProductDetails;
import com.assignment.retail.model.ProductTypeEnum;
import com.assignment.retail.model.User;
import com.assignment.retail.model.UserTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BillServiceTest {
  @MockBean
  private UserRepository userRepository;

  @MockBean
  private ProductRepository productRepository;

  @MockBean
  private OrderRepository orderRepository;

  @Autowired
  private BillService billService;

  @AfterEach
  public void verify() {
    Mockito.verify(productRepository, times(1)).findAllByIds(any());
  }

  @BeforeEach
  public void beforeTest() {
    Mockito.clearAllCaches();
  }

  @Test
  public void testWhenUserIsAnonymous() {
    String userName = "ahmad";
    List<Integer> productId = Collections.singletonList(1);
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.empty());
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(Product.builder().id(1).productType(ProductTypeEnum.GROCERIES).name("Fruits").price(10.0).build()));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.empty());

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 20.0, 0);

    Mockito.verify(userRepository, times(1)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(1)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserHaveDiscountButTheGoodNotIncludedIn() {
    String userName = "ahmad";
    List<Integer> productId = Collections.singletonList(1);
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.EMPLOYEE).userName("ahmad").build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(Product.builder().id(1).productType(ProductTypeEnum.GROCERIES).name("Fruits").price(10.0).build()));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.empty());

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 20.0, 0);

    Mockito.verify(userRepository, times(1)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserHaveEmployeeDiscount() {
    String userName = "ahmad";
    List<Integer> productId = Collections.singletonList(1);
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.EMPLOYEE).userName("ahmad").build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(Product.builder().id(1).productType(ProductTypeEnum.SOFT_GOODS).name("Clothes").price(10.0).build()));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.empty());

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 14.0, 0);

    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserHaveAffiliateDiscount() {
    String userName = "ahmad";
    List<Integer> productId = Collections.singletonList(1);
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.AFFILIATE).userName("ahmad").build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(Product.builder().id(1).productType(ProductTypeEnum.SOFT_GOODS).name("Clothes").price(10.0).build()));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.empty());

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 18.0, 0);

    Mockito.verify(userRepository, times(1)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserHaveTotalDiscount() {
    String userName = "ahmad";
    List<Integer> productId = Collections.singletonList(1);
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.AFFILIATE).userName(userName).build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(Product.builder().id(1).productType(ProductTypeEnum.SOFT_GOODS).name("Clothes").price(200.0).build()));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.of(Order.builder()
            .id(2)
            .userName(userName)
            .orderTime(LocalDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(0, 0, 0, 0)))
            .address("address")
            .email("email@test.com")
            .phoneNumber("0786623232300")
            .totalOrderPrice(300.0)
            .products(Collections.singletonList(ProductDetails.builder().name("Clothes").id(2).amount(3).build()))
            .build()));

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Collections.singletonList(ProductDto.builder().productId(1).amount(2).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 360.0, 0);

    Mockito.verify(userRepository, times(1)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserHaveMoreThanDiscount() {
    String userName = "ahmad";
    List<Integer> productId = Arrays.asList(1, 2);
    Product clothes = Product.builder().id(1).productType(ProductTypeEnum.SOFT_GOODS).name("Clothes").price(100.0).build();
    Product fruit = Product.builder().id(2).productType(ProductTypeEnum.GROCERIES).name("fruit").price(100.0).build();
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.EMPLOYEE).userName(userName).build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Arrays.asList(clothes, fruit));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.of(Order.builder()
            .id(2)
            .userName(userName)
            .orderTime(LocalDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(0, 0, 0, 0)))
            .address("address")
            .email("email@test.com")
            .phoneNumber("0786623232300")
            .totalOrderPrice(300.0)
            .products(Collections.singletonList(ProductDetails.builder().name("Clothes").id(2).amount(3).build()))
            .build()));

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Arrays.asList(ProductDto.builder().productId(1).amount(2).build(), ProductDto.builder().productId(2).amount(1).build()))
            .build();

    NetPayablePayment netPayablePayment = billService.calculateBillWithDiscount(billDetailsDto);

    Assertions.assertEquals(netPayablePayment.getTotalPrice(), 240.0, 0);

    Mockito.verify(userRepository, times(1)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }

  @Test
  public void testWhenUserWantToBuyProductNotFoundInStore() {
    String userName = "ahmad";
    List<Integer> productId = Arrays.asList(1, 2);
    Product clothes = Product.builder().id(1).productType(ProductTypeEnum.SOFT_GOODS).name("Clothes").price(100.0).build();
    Mockito.when(userRepository.findUserByName(userName)).thenReturn(Optional.of(User.builder().userType(UserTypeEnum.EMPLOYEE).userName(userName).build()));
    Mockito.when(productRepository.findAllByIds(productId)).thenReturn(Collections.singletonList(clothes));
    Mockito.when(orderRepository.findOldestOrderByUserName(userName)).thenReturn(Optional.of(Order.builder()
            .id(2)
            .userName(userName)
            .orderTime(LocalDateTime.of(LocalDate.of(2015, 1, 1), LocalTime.of(0, 0, 0, 0)))
            .address("address")
            .email("email@test.com")
            .phoneNumber("0786623232300")
            .totalOrderPrice(300.0)
            .products(Collections.singletonList(ProductDetails.builder().name("Clothes").id(2).amount(3).build()))
            .build()));

    BillDetailsDto billDetailsDto = BillDetailsDto.builder()
            .userName(userName)
            .products(Arrays.asList(ProductDto.builder().productId(1).amount(2).build(), ProductDto.builder().productId(2).amount(1).build()))
            .build();

    Assertions.assertThrows(ElementNotFoundException.class, () -> billService.calculateBillWithDiscount(billDetailsDto));
    Mockito.verify(userRepository, times(0)).findUserByName(anyString());
    Mockito.verify(orderRepository, times(0)).findOldestOrderByUserName(userName);
  }
}
