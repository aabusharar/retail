package com.assignment.retail.adapter.repository.entity;

import com.assignment.retail.model.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductEntity {
  @Id
  private Integer id;

  @Column(unique = true, nullable = false)
  private String name;

  private Double price;

  @Enumerated(EnumType.STRING)
  private ProductTypeEnum productType;
}
