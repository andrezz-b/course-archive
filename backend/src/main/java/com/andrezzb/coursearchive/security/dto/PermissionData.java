package com.andrezzb.coursearchive.security.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PermissionData {
  private boolean granted;
  private boolean grantedByParent;
}
