package com.andrezzb.coursearchive.security.dto;

import com.andrezzb.coursearchive.mappings.ApplicationObjectType;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePermissionDto {
  @NotBlank(message = "Object type is required")
  @ValidEnum(enumClazz = ApplicationObjectType.class, ignoreCase = true)
  private String objectType;
  @NotNull(message = "Object ID is required")
  private Long objectId;
  @NotBlank(message = "Permission is required")
  @ValidEnum(enumClazz = AclPermission.PermissionType.class, ignoreCase = true)
  private String permission;
  @NotBlank(message = "Username is required")
  private String username;
  @NotNull
  private Boolean granting;
}
