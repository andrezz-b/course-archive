package com.andrezzb.coursearchive.material.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TagCreateDto {
  @NotBlank(message = "Tag name is required")
  private String name;
  @NotNull(message = "Course year id is required")
  private Long courseYearId;
}
