package com.andrezzb.coursearchive.security.dto;

import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleChangeDto {
  @NotBlank(message = "Username is required")
  private String username;
  @ValidEnum(enumClazz = Role.RoleName.class, ignoreCase = true)
  private String role;
  @NotNull
  private Boolean granting;
}
