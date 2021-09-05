package com.assignment.retail.adapter.service.impl;

import com.assignment.retail.adapter.repository.OrderRepository;
import com.assignment.retail.adapter.repository.ProductRepository;
import com.assignment.retail.adapter.repository.UserRepository;
import com.assignment.retail.adapter.rest.dto.BillDetailsDto;
import com.assignment.retail.adapter.rest.dto.ProductDto;
import com.assignment.retail.adapter.service.BillService;
import com.assignment.retail.exception.ElementNotFoundException;
import com.assignment.retail.model.NetPayablePayment;
import com.assignment.retail.model.Order;
import com.assignment.retail.model.Product;
import com.assignment.retail.model.User;
import com.assignment.retail.model.UserTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  @Override
  public NetPayablePayment calculateBillWithDiscount(BillDetailsDto billDetailsDto) {
    Map<Integer, Integer> productDtoMap = billDetailsDto.getProducts()
            .stream()
            .collect(Collectors.groupingBy(ProductDto::getProductId))
            .values()
            .stream()
            .map(u -> u.stream().reduce((prod1, prod2) -> new ProductDto(prod1.getProductId(), prod1.getAmount() + prod2.getAmount())).get())
            .collect(Collectors.toMap(ProductDto::getProductId, ProductDto::getAmount));

    List<Product> products = getAllProductDetails(billDetailsDto.getProducts());

    // apply user discount (type discount)
    double userTypeDiscountPercentage = getUserTypeDiscountPercentage(billDetailsDto.getUserName());
    double sumOfEligibleProductsForUserTypeDiscount = products.stream().filter(u -> u.getProductType().isDiscountApplicable()).mapToDouble(u -> u.getPrice() * productDtoMap.getOrDefault(u.getId(), 0)).sum();
    double sumOfNonEligibleProductsForUserTypeDiscount = products.stream().filter(u -> !u.getProductType().isDiscountApplicable()).mapToDouble(u -> u.getPrice() * productDtoMap.getOrDefault(u.getId(), 0)).sum();
    double totalAmountAfterApplyingUserTypeDiscount = applyUserTypeDiscount(sumOfEligibleProductsForUserTypeDiscount, userTypeDiscountPercentage, sumOfNonEligibleProductsForUserTypeDiscount);

    // apply discount when bill more than 100$
    double totalAmountAfterApplyingOver100HinderedDiscount = applyBillDiscount(products.stream().mapToDouble(u -> u.getPrice() * productDtoMap.getOrDefault(u.getId(), 0)).sum());

    return totalAmountAfterApplyingUserTypeDiscount < totalAmountAfterApplyingOver100HinderedDiscount
            ? NetPayablePayment.builder().totalPrice(totalAmountAfterApplyingUserTypeDiscount).build()
            : NetPayablePayment.builder().totalPrice(totalAmountAfterApplyingOver100HinderedDiscount).build();
  }

  private double applyUserTypeDiscount(double eligiblePriceForDiscount, double discountPercentage, double nonEligiblePriceForDiscount) {
    return nonEligiblePriceForDiscount + eligiblePriceForDiscount - (eligiblePriceForDiscount * discountPercentage);
  }

  private double applyBillDiscount(double amount) {
    if (amount > 100.0) {
      return amount - (int) (amount * 5 / 100);
    } else {
      return amount;
    }
  }

  private double getUserTypeDiscountPercentage(String userName) {
    User user = getUserByUserName(userName);
    double percentage = user.getUserType().getPercentage();

    if(percentage == 0 && isUserForMoreThanTwoYears(userName)) {
      percentage = 0.5;
    }

    return percentage;
  }

  private Boolean isUserForMoreThanTwoYears(String userName) {
    Optional<Order> oldestOrderByUserName = orderRepository.findOldestOrderByUserName(userName);
    return oldestOrderByUserName.map(order -> {
      return order.getOrderTime().until(LocalDateTime.now(), ChronoUnit.YEARS) >= 2;
    }).orElse(false);
  }

  private User getUserByUserName(String userName) {
    return userRepository.findUserByName(userName).orElse(User.builder().userName(userName).userType(UserTypeEnum.ANONYMOUS).build());
  }

  private List<Product> getAllProductDetails(List<ProductDto> productDetails) {
    List<Integer> productIds = productDetails.stream().map(ProductDto::getProductId).distinct().collect(Collectors.toList());
    List<Product> allExistProducts = productRepository.findAllByIds(productIds);
    List<String> nonExistProducts = productIds.stream().filter(u -> allExistProducts.stream().noneMatch(f -> f.getId().equals(u)))
            .map(Object::toString).collect(Collectors.toList());
    if (!nonExistProducts.isEmpty()) {
      throw new ElementNotFoundException(String.format("the following products not exist [%s]", String.join(",", nonExistProducts)));
    }
    return allExistProducts;
  }
}
