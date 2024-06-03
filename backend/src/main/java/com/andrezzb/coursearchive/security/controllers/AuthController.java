package com.andrezzb.coursearchive.security.controllers;

import com.andrezzb.coursearchive.security.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
  public ResponseEntity<LoginDto.LoginResponse> login(
    @Valid @RequestBody LoginDto.LoginBody loginData) {
    final var tokenResponse = authService.login(loginData.getUsername(), loginData.getPassword());
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<Long> register(@Valid @RequestBody RegisterDto registerData) {
    final UserEntity user = authService.register(registerData);
    return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
  }

  @PostMapping("/permission")
  public ResponseEntity<Void> grantPermission(
    @Valid @RequestBody ChangePermissionDto changePermissionDto) {
    authService.changePermission(changePermissionDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/role")
  public ResponseEntity<Void> grantRole(@Valid @RequestBody RoleChangeDto roleDto) {
    if (roleDto.getGranting()) {
      authService.grantRole(roleDto.getUsername(), roleDto.getRole());
    } else {
      authService.revokeRole(roleDto.getUsername(), roleDto.getRole());
    }
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshDto.Response> refresh(
    @Valid @RequestBody RefreshDto.Body refreshData) {
    final String token = authService.refresh(refreshData.getRefreshToken());
    return ResponseEntity.ok(RefreshDto.Response.builder().accessToken(token).build());
  }
}
