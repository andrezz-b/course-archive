package com.andrezzb.coursearchive.material.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
  private Long id;
  private String text;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean edited;
  private boolean isCurrentUser;
  private String username;
}
