package com.andrezzb.coursearchive.material.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDataDto {
  @NotBlank(message = "Comment text is required")
  private String text;
}
