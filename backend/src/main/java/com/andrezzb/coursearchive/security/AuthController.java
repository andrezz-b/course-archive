package com.andrezzb.coursearchive.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.security.dto.GrantPermissionDto;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.dto.RefreshDto;
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
  public ResponseEntity<LoginDto.LoginResponse> login(@Valid @RequestBody LoginDto.LoginBody loginData) {
    final var tokenResponse = authService.login(loginData.getUsername(), loginData.getPassword());
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<Long> register(@Valid @RequestBody RegisterDto registerData) {
    final UserEntity user = authService.register(registerData);
    return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
  }

  @PostMapping("/grant-permission")
  public ResponseEntity<Void> grantPermission(@Valid @RequestBody GrantPermissionDto grantPermissionDto) {
    authService.grantPermission(grantPermissionDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshDto.Response> refresh(@Valid @RequestBody RefreshDto.Body refreshData) {
    final String token = authService.refresh(refreshData.getRefreshToken());
    return ResponseEntity.ok(RefreshDto.Response.builder().accessToken(token).build());
  }
}
