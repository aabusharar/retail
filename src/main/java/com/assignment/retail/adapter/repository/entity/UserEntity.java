package com.assignment.retail.adapter.repository.entity;

import com.assignment.retail.model.UserTypeEnum;
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
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity {

  @Id
  private Integer id;

  @Column(unique = true, nullable = false)
  private String userName;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private UserTypeEnum type;
}
