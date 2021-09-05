package com.assignment.retail.adapter.repository;

import com.assignment.retail.adapter.TestConfig;
import com.assignment.retail.adapter.repository.entity.UserEntity;
import com.assignment.retail.adapter.repository.jpa.dao.UserRepositoryDao;
import com.assignment.retail.model.User;
import com.assignment.retail.model.UserTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@Import(TestConfig.class)
@TestPropertySource("classpath:application-test.yml")
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepositoryDao userRepositoryDao;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void beforeTests() {
    userRepositoryDao.deleteAll();

    String userName = "registeredUser";
    String phoneNumber = "xxxxxxxxxxxxxxxxxxxxxxx";
    String email = "registeredUser@test.com";
    String name = "xxx";

    UserEntity user = UserEntity.builder()
            .id(1)
            .userName(userName)
            .phoneNumber(phoneNumber)
            .email(email)
            .lastName(name)
            .firstName(name)
            .type(UserTypeEnum.EMPLOYEE)
            .build();

    userRepositoryDao.save(user);
  }

  @Test
  public void testWhenUserNotFound() {
    String userName = "notFoundUser";
    Optional<User> userByName = userRepository.findUserByName(userName);
    Assertions.assertFalse(userByName.isPresent());
  }

  @Test
  public void testWhenUserExist() {
    String userName = "registeredUser";
    String phoneNumber = "xxxxxxxxxxxxxxxxxxxxxxx";
    String email = "registeredUser@test.com";
    String name = "xxx";

    Optional<User> userByName = userRepository.findUserByName(userName);
    Assertions.assertTrue(userByName.isPresent());
    User user = userByName.get();
    Assertions.assertSame(user.getUserName(), userName);
    Assertions.assertSame(user.getPhoneNumber(), phoneNumber);
    Assertions.assertSame(user.getUserType(), UserTypeEnum.EMPLOYEE);
    Assertions.assertSame(user.getEmail(), email);
    Assertions.assertSame(user.getFirstName(), name);
    Assertions.assertSame(user.getLastName(), name);
  }
}
