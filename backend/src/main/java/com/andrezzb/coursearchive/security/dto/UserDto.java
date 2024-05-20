package com.andrezzb.coursearchive.security.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private List<String> roles;
}
