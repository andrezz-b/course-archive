package com.andrezzb.coursearchive.security.dto;

import java.util.Set;

import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private Set<String> roles;
}
