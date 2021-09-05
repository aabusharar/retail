package com.assignment.retail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class User {

  private Integer id;

  private String userName;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private UserTypeEnum userType;
}
