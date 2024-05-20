package com.andrezzb.coursearchive.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.security.dto.GrantPermissionDto;
import com.andrezzb.coursearchive.security.dto.LoginDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.mappings.GrantPermissionMapping;
import com.andrezzb.coursearchive.security.models.AclSecured;
import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import java.util.Arrays;
import org.springframework.security.acls.model.Permission;


@Service
public class AuthService {

  public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,30}$";

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final GrantPermissionMapping grantPermissionMapping;
  private final AclUtilService aclUtilService;
  private final UserService userService;

  public AuthService(TokenService tokenService, AuthenticationManager authenticationManager,
      UserRepository userRepository, PasswordEncoder passwordEncoder,
      GrantPermissionMapping grantPermissionMapping, AclUtilService aclUtilService,
      UserService userService) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.grantPermissionMapping = grantPermissionMapping;
    this.aclUtilService = aclUtilService;
    this.userService = userService;
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
    user.setRoles(Arrays.asList(userService.findRoleByName(Role.RoleName.USER)));
    return userRepository.save(user);
  }

  @Transactional
  public void grantPermission(GrantPermissionDto grantPermissionDto) {
    var repository = grantPermissionMapping.getRepository(grantPermissionDto.getObjectType());
    Permission permission =
        grantPermissionMapping.getPermission(grantPermissionDto.getPermission());
    AclSecured object =
        (AclSecured) repository.findById(grantPermissionDto.getObjectId()).orElseThrow(
            () -> new IllegalArgumentException("Invalid object ID"));
    Boolean userExists = userRepository.existsByUsername(grantPermissionDto.getUsername());
    if (!userExists) {
      throw new IllegalArgumentException("Invalid username");
    }

    aclUtilService.grantPermission(object, grantPermissionDto.getUsername(), permission);

    // ObjectIdentity oi = new ObjectIdentityImpl(objectClass, grantPermissionDto.getObjectId());
    // Sid sid = new PrincipalSid(grantPermissionDto.getUsername());
    // MutableAcl acl = null;
    // try {
    // acl = (MutableAcl) aclService.readAclById(oi);
    // } catch (NotFoundException nfe) {
    // acl = aclService.createAcl(oi);
    // }

    // // Now grant some permissions via an access control entry (ACE)
    // acl.insertAce(acl.getEntries().size(), permission, sid, true);
    // aclService.updateAcl(acl);
  }

  public String refresh(String refreshToken) {
    var jwt = tokenService.validateRefreshToken(refreshToken);
    var user = userService.findByUsername(jwt.getSubject());
    Authentication authentication = UsernamePasswordAuthenticationToken
        .authenticated(user.getUsername(), user.getPassword(), user.getAuthorities());
    return tokenService.generateToken(authentication);
  }

}
