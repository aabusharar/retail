package com.assignment.retail.adapter.repository.jpa;

import com.assignment.retail.adapter.repository.ProductRepository;
import com.assignment.retail.adapter.repository.entity.ProductEntity;
import com.assignment.retail.adapter.repository.jpa.dao.ProductRepositoryDao;
import com.assignment.retail.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryJPAImpl implements ProductRepository {
  private final ProductRepositoryDao repo;

  @Override
  public List<Product> findAllByIds(List<Integer> productIds) {
    return repo.findAllByIdIn(productIds).stream().map(this::toModel).collect(Collectors.toList());
  }

  private Product toModel(ProductEntity entity) {
    return Optional.ofNullable(entity)
            .map(e -> Product.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .price(e.getPrice())
                    .productType(e.getProductType())
                    .build()).orElse(Product.builder().build());
  }
}
