package com.andrezzb.coursearchive.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {

  @Mock
  private TokenService tokenService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthService authService;

  @Test
  void givenUsernamePassowrd_whenLogin_thenReturnToken() {
    final String username = "testuser";
    final String password = "testpassword";
    final String returnedToken = "token";
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenReturn(UsernamePasswordAuthenticationToken.unauthenticated(username, password));
    when(tokenService.generateToken(any(Authentication.class))).thenReturn(returnedToken);

    String token = authService.login("testuser", "testpassword");

    assertThat(token).isEqualTo(returnedToken);
  }

  @Test
  void givenInvalidCredentials_whenLogin_thenThrowAuthenticationException() {
    final String username = "testuser";
    final String password = "testpassword";
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(BadCredentialsException.class);

    assertThatThrownBy(() -> authService.login(username, password))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void givenValidRegisterData_whenRegister_thenReturnRegisteredUser() {
    RegisterDto registerData = RegisterDto.builder()
        .username("testuser")
        .email("test@example.com")
        .password("TestPassword123")
        .build();

    UserEntity registeredUser = new UserEntity();
    registeredUser.setId(1L);
    registeredUser.setUsername(registerData.getUsername());
    registeredUser.setEmail(registerData.getEmail());

    when(userRepository.findByUsername(registerData.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(registerData.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(registerData.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(UserEntity.class))).thenReturn(registeredUser);

    UserEntity result = authService.register(registerData);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(registeredUser.getId());
    assertThat(result.getUsername()).isEqualTo(registeredUser.getUsername());
    assertThat(result.getEmail()).isEqualTo(registeredUser.getEmail());

    verify(userRepository).findByUsername(registerData.getUsername());
    verify(userRepository).findByEmail(registerData.getEmail());
    verify(passwordEncoder).encode(registerData.getPassword());
    verify(userRepository).save(any(UserEntity.class));
  }

  @Test
  void givenExistingUsername_whenRegister_thenThrowUsernameTakenException() {
    RegisterDto registerData = RegisterDto.builder()
        .username("testuser")
        .email("test@example.com")
        .password("TestPassword123")
        .build();


    when(userRepository.findByEmail(registerData.getEmail()))
        .thenReturn(Optional.empty());
    when(userRepository.findByUsername(registerData.getUsername()))
        .thenReturn(Optional.of(new UserEntity()));

    assertThatThrownBy(() -> authService.register(registerData))
        .isInstanceOf(UsernameTakenException.class);

    verify(userRepository).findByEmail(registerData.getEmail());
    verify(userRepository).findByUsername(registerData.getUsername());
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));
  }

  @Test
  void givenExistingEmail_whenRegister_thenThrowEmailTakenException() {
    RegisterDto registerData = RegisterDto.builder()
        .username("testuser")
        .email("test@example.com")
        .password("TestPassword123")
        .build();

    when(userRepository.findByEmail(registerData.getEmail()))
        .thenReturn(Optional.of(new UserEntity()));

    assertThatThrownBy(() -> authService.register(registerData))
        .isInstanceOf(EmailTakenException.class);

    verify(userRepository).findByEmail(registerData.getEmail());
    verify(userRepository, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));
  }

}
