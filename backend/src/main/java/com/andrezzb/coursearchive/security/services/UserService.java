package com.andrezzb.coursearchive.security.services;

import com.andrezzb.coursearchive.security.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.security.exceptions.UserNotFoundException;
import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.RoleRepository;
import com.andrezzb.coursearchive.security.repository.UserRepository;

import java.util.List;


@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;

  public UserService(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
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

  @PreAuthorize("hasRole('ADMIN')")
  public Page<UserDto> findAllUsersPaged(Pageable p, List<String> filterFields, List<Object> filterValueObjs) {
    var users = userRepository.findAllByFilterFieldAndFilterValue(p, filterFields, filterValueObjs);
    return users.map(user -> modelMapper.map(user, UserDto.class));
  }
}
