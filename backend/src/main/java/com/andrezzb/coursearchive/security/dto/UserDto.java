package com.andrezzb.coursearchive.security.dto;

import java.util.List;
import com.andrezzb.coursearchive.security.models.Role;
import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private List<Role> roles;
}
