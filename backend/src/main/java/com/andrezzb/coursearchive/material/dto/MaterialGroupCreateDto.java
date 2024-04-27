package com.andrezzb.coursearchive.material.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialGroupCreateDto {
  @NotBlank(message = "Name is required")
  @Size(max = 64, message = "Name must be at most 64 characters")
  private String name;

  @NotNull(message = "Course year id is required")
  private Long courseYearId;

  @Min(value = 0, message = "Order must be at least 0")
  private Short order;

  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;
}
