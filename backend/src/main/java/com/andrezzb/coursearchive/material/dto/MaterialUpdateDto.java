package com.andrezzb.coursearchive.material.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialUpdateDto {
  @Size(max = 64, message = "Name must be at most 64 characters")
  private String name;

  private Long materialGroupId;

  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;
}
