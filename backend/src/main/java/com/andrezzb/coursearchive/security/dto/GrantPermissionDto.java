package com.andrezzb.coursearchive.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrantPermissionDto {
  @NotBlank(message = "Object type is required")
  private String objectType;
  @NotNull(message = "Object ID is required")
  private Long objectId;
  @NotBlank(message = "Permission is required")
  private String permission;
  @NotBlank(message = "Username is required")
  private String username;
}
