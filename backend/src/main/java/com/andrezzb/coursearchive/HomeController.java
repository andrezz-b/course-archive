package com.andrezzb.coursearchive;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api")
public class HomeController {

  @GetMapping("/")
  public String home(Authentication principal) {
    return "Welcome, " + principal.getName();
  }

}
