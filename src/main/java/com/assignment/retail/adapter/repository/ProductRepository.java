package com.assignment.retail.adapter.repository;

import com.assignment.retail.model.Product;

import java.util.List;

public interface ProductRepository {
  List<Product> findAllByIds(List<Integer> productIds);
}