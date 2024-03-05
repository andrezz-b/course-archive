package com.andrezzb.coursearchive.security.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.andrezzb.coursearchive.security.AuthController;
import com.andrezzb.coursearchive.security.SecurityConfig;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import com.andrezzb.coursearchive.security.services.AuthService;
import com.andrezzb.coursearchive.security.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, CustomUserDetailsService.class})
class LoginAuthControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @MockBean
  private UserRepository userRepository;

  private static final String LOGIN_URL = "/api/auth/login";

  @Test
  void givenMissingUsername_whenLogin_thenShouldFail() throws Exception {
    LoginDto loginData = LoginDto.builder()
        .password("testpassword")
        .build();

    this.mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginData)))
        .andExpectAll(
            MockMvcResultMatchers.status().isBadRequest(),
            MockMvcResultMatchers.jsonPath("$.errors[0]").value("Username is required"));
  }

  @Test
  void givenMissingPassword_whenLogin_thenShouldFail() throws Exception {
    LoginDto loginData = LoginDto.builder()
        .username("testuser")
        .build();

    this.mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginData)))
        .andExpectAll(
            MockMvcResultMatchers.status().isBadRequest(),
            MockMvcResultMatchers.jsonPath("$.errors[0]").value("Password is required"));
  }

  @Test
  void givenValidCredentials_whenLogin_thenShouldReturnToken() throws Exception {
    LoginDto loginData = LoginDto.builder()
        .username("testuser")
        .password("testpassword")
        .build();

    when(authService.login(anyString(), anyString())).thenReturn("testtoken");

    this.mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginData)))
        .andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.content().string("testtoken"));
  }
}
