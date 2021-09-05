package com.assignment.retail.adapter.repository.jpa.dao;

import com.assignment.retail.adapter.repository.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepositoryDao extends JpaRepository<OrderEntity, Integer> {

  Page<OrderEntity> findAllByUserNameIgnoreCase(String userName, Pageable pageable);
}