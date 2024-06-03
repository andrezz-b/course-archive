package com.andrezzb.coursearchive.security.services;

import com.andrezzb.coursearchive.mappings.ApplicationObjectType;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.repository.RoleRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.security.dto.ChangePermissionDto;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.models.AclSecured;
import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.acls.model.Permission;


@Service
public class AuthService {

  public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,30}$";

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AclUtilService aclUtilService;
  private final UserService userService;
  private final ApplicationContext context;
  private final RoleRepository roleRepository;

  public AuthService(TokenService tokenService, AuthenticationManager authenticationManager,
    UserRepository userRepository, PasswordEncoder passwordEncoder, AclUtilService aclUtilService,
    UserService userService, ApplicationContext applicationContext, RoleRepository roleRepository) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.aclUtilService = aclUtilService;
    this.userService = userService;
    this.context = applicationContext;
    this.roleRepository = roleRepository;
  }

  public LoginDto.LoginResponse login(String username, String password) {
    Authentication authenticationRequest =
      UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    Authentication authenticationResponse =
      this.authenticationManager.authenticate(authenticationRequest);
    String accessToken = tokenService.generateToken(authenticationResponse);
    String refreshToken = tokenService.generateRefreshToken(authenticationResponse);
    return LoginDto.LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
      .build();
  }

  public UserEntity register(RegisterDto registerData) {
    if (userService.existsByEmail(registerData.getEmail())) {
      throw new EmailTakenException(registerData.getEmail());
    }
    if (userService.existsByUsername(registerData.getUsername())) {
      throw new UsernameTakenException(registerData.getUsername());
    }
    UserEntity user = new UserEntity();
    user.setUsername(registerData.getUsername());
    user.setPassword(passwordEncoder.encode(registerData.getPassword()));
    user.setFirstName(registerData.getFirstName());
    user.setLastName(registerData.getLastName());
    user.setEmail(registerData.getEmail());
    user.setRoles(Collections.singleton(userService.findRoleByName(Role.RoleName.USER)));
    return userRepository.save(user);
  }

  @Transactional
  public void changePermission(ChangePermissionDto changePermissionDto) {
    ApplicationObjectType objectType =
      ApplicationObjectType.fromString(changePermissionDto.getObjectType());
    var repository = context.getBean(objectType.getRepositoryClass());
    Permission permission = AclPermission.fromString(changePermissionDto.getPermission());
    AclSecured object = repository.findById(changePermissionDto.getObjectId())
      .orElseThrow(() -> new IllegalArgumentException("Invalid object ID"));
    boolean userExists = userRepository.existsByUsername(changePermissionDto.getUsername());
    if (!userExists) {
      throw new IllegalArgumentException("Invalid username");
    }

    if (changePermissionDto.getGranting()) {
      aclUtilService.grantPermission(object, changePermissionDto.getUsername(), permission);
    } else {
      aclUtilService.revokePermission(object, changePermissionDto.getUsername(), permission);
    }
  }

  public String refresh(String refreshToken) {
    var jwt = tokenService.validateRefreshToken(refreshToken);
    var user = userService.findByUsername(jwt.getSubject());
    Authentication authentication =
      UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), user.getPassword(),
        user.getAuthorities());
    return tokenService.generateToken(authentication);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void grantRole(String username, String roleNameString) {
    var user = userService.findByUsername(username);
    Role.RoleName roleName = Role.RoleName.fromString(roleNameString);

    // Ignore if role exists
    for (var role : user.getRoles()) {
      if (role.getName().equals(roleName.toString())) {
        return;
      }
    }
    Role role = roleRepository.findByName(roleName.toString());
    user.getRoles().add(role);
    userRepository.save(user);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void revokeRole(String username, String roleNameString) {
    var user = userService.findByUsername(username);
    Role.RoleName roleName = Role.RoleName.fromString(roleNameString);
    var filteredRoles =
      user.getRoles().stream().filter(role -> !role.getName().equals(roleName.toString()))
        .collect(Collectors.toSet());
    user.setRoles(filteredRoles);
    userRepository.save(user);
  }
}
