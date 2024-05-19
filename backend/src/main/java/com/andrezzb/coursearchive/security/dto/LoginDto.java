package com.andrezzb.coursearchive.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


public class LoginDto {

  @Data
  @Builder
  public static class LoginBody {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
  }

  @Data
  @Builder
  public static class LoginResponse {
    private String accessToken;
    private String refreshToken;
  }
}


