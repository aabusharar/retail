package com.assignment.retail.adapter.repository.entity;

import com.assignment.retail.adapter.repository.convertor.ProductDetailsConvertor;
import com.assignment.retail.model.ProductDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderEntity {

  @Id
  private Integer id;

  private String userName;

  private String address;

  private String email;

  private String phoneNumber;

  private LocalDateTime orderTime;

  private Double totalOrderPrice;

  @Convert(converter = ProductDetailsConvertor.class)
  @Column(columnDefinition = "json")
  private List<ProductDetails> products;
}
