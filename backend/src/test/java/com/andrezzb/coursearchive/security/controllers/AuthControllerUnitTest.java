package com.andrezzb.coursearchive.security.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.andrezzb.coursearchive.exceptions.ErrorObject;
import com.andrezzb.coursearchive.security.AuthController;
import com.andrezzb.coursearchive.security.SecurityConfig;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto.RegisterDtoBuilder;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import com.andrezzb.coursearchive.security.services.AuthService;
import com.andrezzb.coursearchive.security.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, CustomUserDetailsService.class})
public class AuthControllerUnitTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @MockBean
  private UserRepository userRepository;



  @Nested
  @DisplayName("Login")
  class Login {
    private static final String LOGIN_URL = "/api/auth/login";

    @Test
    void givenMissingUsername_whenLogin_thenShouldFail() throws Exception {
      LoginDto loginData = LoginDto.builder()
          .password("testpassword")
          .build();

      mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
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

      mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
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

      mvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(loginData)))
          .andExpectAll(
              MockMvcResultMatchers.status().isOk(),
              MockMvcResultMatchers.content().string("testtoken"));
    }
  }

  @Nested
  @DisplayName("Register")
  class Register {
    private static final String REGISTER_URL = "/api/auth/register";

    private RegisterDtoBuilder defaultData = RegisterDto.builder()
        .firstName("first")
        .lastName("last")
        .username("testuser")
        .email("testmail@test.com")
        .password("Test01234")
        .passwordRepeat("Test01234");

    @Test
    void givenRegisterData_whenRegister_thenReturnCreatedWithUserId() throws Exception {
      RegisterDto registerData = defaultData.build();

      UserEntity mockEntity = new UserEntity();
      mockEntity.setId(5L);
      when(authService.register(registerData)).thenReturn(mockEntity);

      mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(registerData)))
          .andExpectAll(
              MockMvcResultMatchers.status().isCreated(),
              MockMvcResultMatchers.content().string("5"));
    }

    @Test
    void givenExistingUsername_whenRegister_thenShouldFail() throws Exception {
      RegisterDto registerData = defaultData.build();

      when(authService.register(registerData))
          .thenThrow(new UsernameTakenException(registerData.getUsername()));

      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(registerData)))
          .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);

      assertThat(responseMap.getErrors())
          .contains("Username " + registerData.getUsername() + " is already taken.");
    }

    @Test
    void givenExistingEmail_whenRegister_thenShouldFail() throws Exception {
      RegisterDto registerData = defaultData.build();

      when(authService.register(registerData))
          .thenThrow(new EmailTakenException(registerData.getEmail()));

      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(registerData)))
          .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);

      assertThat(responseMap.getErrors())
          .contains("Email " + registerData.getEmail() + " is already taken.");
    }



    @Nested
    @DisplayName("Register body validation")
    class RegisterBodyValidation {
      @Test
      void givenMissingFirstName_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .firstName(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("First name is required");
      }

      @Test
      void givenMissingLastName_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .lastName(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Last name is required");
      }

      @Test
      void givenMissingUsername_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .username(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Username is required");
      }

      @Test
      void givenMissingEmail_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .email(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Email is required");
      }

      @Test
      void givenMissingPasswordRepeat_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .passwordRepeat(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Password confirmation is required");
      }


      @Test
      void givenMissingPassword_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .password(null)
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Password is required");
      }

      @Test
      void givenPasswordRepeatMismatch_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .passwordRepeat("DifferentPassword")
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("Passwords must match");
      }

      @Test
      void givenInvalidEmail_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .email("invalidemail")
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains("The email address is invalid");
      }

      @Test
      void givenInvalidPassword_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .password("invalidpassword")
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors()).contains(
            "Password must contain at least one uppercase letter, one lowercase letter, one number and be between 8 and 30 characters");
      }

      @Test
      void givenUsernameTooShort_whenRegister_thenShouldFail() throws Exception {
        RegisterDto registerData = defaultData
            .username("us")
            .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerData)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorObject responseMap =
            objectMapper.readValue(content, ErrorObject.class);

        assertThat(responseMap.getErrors())
            .contains("Username must be between 3 and 20 characters");
      }
    }
  }
}
