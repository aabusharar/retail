package com.assignment.retail.adapter.repository.jpa.dao;

import com.assignment.retail.adapter.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryDao extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByUserNameIgnoreCase(String userName);
}