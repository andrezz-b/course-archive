package com.andrezzb.coursearchive.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginDto loginData) {
    return authService.login(loginData.getUsername(), loginData.getPassword());
  }

  @PostMapping("/register")
  public UserEntity register(@Valid @RequestBody RegisterDto registerData) {
    return authService.register(registerData);
  }
}
