package com.andrezzb.coursearchive.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  public AuthService(TokenService tokenService, AuthenticationManager authenticationManager) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
  }

  public String login(String username, String password) {
    Authentication authenticationRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    Authentication authenticationResponse =
        this.authenticationManager.authenticate(authenticationRequest);
    String token = tokenService.generateToken(authenticationResponse);
    return token;
  }

}
