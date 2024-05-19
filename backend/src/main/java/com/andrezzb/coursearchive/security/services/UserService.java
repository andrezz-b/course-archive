package com.andrezzb.coursearchive.security.services;

import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.security.exceptions.UserNotFoundException;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;



@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean existsByUsername(String username) {
    var userOptional = userRepository.findByUsername(username);
    return userOptional.isPresent();
  }

  public boolean existsByEmail(String email) {
    var userOptional = userRepository.findByEmail(email);
    return userOptional.isPresent();
  }

  public UserEntity findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }
}
