package com.assignment.retail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class Order {
  private Integer id;

  private String userName;

  private String address;

  private String email;

  private String phoneNumber;

  private LocalDateTime orderTime;

  private Double totalOrderPrice;

  private List<ProductDetails> products;
}
