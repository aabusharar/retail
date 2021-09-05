package com.assignment.retail.adapter.repository.jpa.dao;

import com.assignment.retail.adapter.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryDao extends JpaRepository<ProductEntity, Integer> {
  List<ProductEntity> findAllByIdIn(List<Integer> productId);
}