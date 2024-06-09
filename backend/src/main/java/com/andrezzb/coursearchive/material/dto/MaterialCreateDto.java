package com.andrezzb.coursearchive.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MaterialCreateDto {
  @NotBlank(message = "Name is required")
  @Size(max = 64, message = "Name must be at most 64 characters")
  private String name;

  @NotNull(message = "Material group id is required")
  private Long materialGroupId;

  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;

  List<Long> tagIds;
}
