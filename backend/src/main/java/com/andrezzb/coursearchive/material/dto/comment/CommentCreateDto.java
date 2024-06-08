package com.andrezzb.coursearchive.material.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDto {
  @NotBlank(message = "Comment text is required")
  private String text;
  @NotNull(message = "Material id is required")
  private Long materialId;
}
