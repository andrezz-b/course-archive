package com.andrezzb.coursearchive.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.security.dto.RegisterDTO;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.services.AuthService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest.username(), loginRequest.password());
  }

  @PostMapping("/register")
  public UserEntity register(@RequestBody RegisterDTO registerData) {
    return authService.register(registerData);
  }

  public record LoginRequest(String username, String password) {
  }
}
