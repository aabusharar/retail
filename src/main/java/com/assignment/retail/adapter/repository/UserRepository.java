package com.assignment.retail.adapter.repository;

import com.assignment.retail.model.User;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findUserByName(String userName);
}
