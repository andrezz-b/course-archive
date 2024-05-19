package com.andrezzb.coursearchive.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public class RefreshDto {

  @Data
  public static class Body {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
  }

  @Data
  @Builder
  public static class Response {
    private String accessToken;
  }
}