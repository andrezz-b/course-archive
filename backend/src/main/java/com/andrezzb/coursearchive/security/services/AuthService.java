package com.andrezzb.coursearchive.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;

@Service
public class AuthService {

  public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,30}$";

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(TokenService tokenService, AuthenticationManager authenticationManager,
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public String login(String username, String password) {
    Authentication authenticationRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    Authentication authenticationResponse =
        this.authenticationManager.authenticate(authenticationRequest);
    String token = tokenService.generateToken(authenticationResponse);
    return token;
  }

  public UserEntity register(RegisterDto registerData) {
    userRepository.findByEmail(registerData.getEmail()).ifPresent(user -> {
      throw new EmailTakenException(registerData.getEmail());
    });
    userRepository.findByUsername(registerData.getUsername()).ifPresent(user -> {
      throw new UsernameTakenException(registerData.getUsername());
    });
    UserEntity user = new UserEntity();
    user.setUsername(registerData.getUsername());
    user.setPassword(passwordEncoder.encode(registerData.getPassword()));
    user.setFirstName(registerData.getFirstName());
    user.setLastName(registerData.getLastName());
    user.setEmail(registerData.getEmail());
    return userRepository.save(user);
  }

}
