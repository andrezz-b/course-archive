package com.andrezzb.coursearchive.security.services;

import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.security.exceptions.UserNotFoundException;
import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.RoleRepository;
import com.andrezzb.coursearchive.security.repository.UserRepository;



@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public UserService(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
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

  public Role findRoleByName(Role.RoleName name) {
    return findRoleByName(name.toString());
  }

  public Role findRoleByName(String name) {
    return roleRepository.findByName(name);
  }
}
