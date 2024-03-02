package com.andrezzb.coursearchive;

import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.security.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api")
@Slf4j
public class HomeController {

  private final TokenService tokenService;


  public HomeController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @GetMapping("/")
  public String home() {
    return "Welcome home";
  }

  @PostMapping("/auth/login")
  public String login(Authentication authentication) {
    log.debug("Token requested for user: '{}'", authentication.getName());
    String token = tokenService.generateToken(authentication);
    log.debug("Token granted: {}", token);
    return token;
  }

}
