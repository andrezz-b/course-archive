package com.andrezzb.coursearchive.security.dto;

import org.hibernate.internal.util.StringHelper;
import com.andrezzb.coursearchive.security.services.AuthService;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "The email address is invalid")
  private String email;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
  private String username;

  @NotBlank(message = "Password is required")
  @Pattern(regexp = AuthService.PASSWORD_REGEX,
      message = "Password must contain at least one uppercase letter, one lowercase letter, one number and be between 8 and 30 characters")
  private String password;

  @NotBlank(message = "Password confirmation is required")
  private String passwordRepeat;

  @AssertTrue(message = "Passwords must match")
  private boolean isPasswordSame() {
    return StringHelper.isNotEmpty(password) && password.equals(passwordRepeat);
  }
}
