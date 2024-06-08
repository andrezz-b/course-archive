package com.andrezzb.coursearchive.material.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagUpdateDto {
  @NotBlank(message = "Tag name is required")
  private String name;
}
