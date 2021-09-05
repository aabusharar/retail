package com.assignment.retail.adapter.repository.jpa;

import com.assignment.retail.adapter.repository.UserRepository;
import com.assignment.retail.adapter.repository.entity.UserEntity;
import com.assignment.retail.adapter.repository.jpa.dao.UserRepositoryDao;
import com.assignment.retail.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryJPAImpl implements UserRepository {
  private final UserRepositoryDao repo;

  @Override
  public Optional<User> findUserByName(String userName) {
    return repo.findByUserNameIgnoreCase(userName).map(this::toModel);
  }

  private User toModel(UserEntity entity) {
    return Optional.ofNullable(entity).map(e -> User.builder()
            .userName(e.getUserName())
            .email(e.getEmail())
            .firstName(e.getFirstName())
            .userType(e.getType())
            .id(e.getId())
            .lastName(e.getLastName())
            .phoneNumber(e.getPhoneNumber())
            .build()).orElseGet(() -> null);
  }
}
