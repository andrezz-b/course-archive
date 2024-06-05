package com.andrezzb.coursearchive.security.dto;

import com.andrezzb.coursearchive.mappings.ApplicationObjectType;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionDataBodyDto {
  @ValidEnum(enumClazz = ApplicationObjectType.class, ignoreCase = true)
  private String objectType;
  @NotNull(message = "objectId is required")
  private Long objectId;
  @NotBlank(message = "Username is required")
  private String username;
}
