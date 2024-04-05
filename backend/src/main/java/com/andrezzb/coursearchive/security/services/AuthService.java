package com.andrezzb.coursearchive.security.services;

import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.security.dto.GrantPermissionDto;
import com.andrezzb.coursearchive.security.dto.RegisterDto;
import com.andrezzb.coursearchive.security.exceptions.EmailTakenException;
import com.andrezzb.coursearchive.security.exceptions.UsernameTakenException;
import com.andrezzb.coursearchive.security.mappings.GrantPermissionMapping;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;


@Service
public class AuthService {

  public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,30}$";

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final GrantPermissionMapping grantPermissionMapping;
  private final MutableAclService aclService;

  public AuthService(TokenService tokenService, AuthenticationManager authenticationManager,
      UserRepository userRepository, PasswordEncoder passwordEncoder,
      GrantPermissionMapping grantPermissionMapping, MutableAclService aclService) {
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.grantPermissionMapping = grantPermissionMapping;
    this.aclService = aclService;
  }

  public String login(String username, String password) {
    Authentication authenticationRequest =
        UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    Authentication authenticationResponse =
        this.authenticationManager.authenticate(authenticationRequest);
    String token = tokenService.generateToken(authenticationResponse);
    return token;
  }

  public UserEntity register(RegisterDto registerData) {
    userRepository.findByEmail(registerData.getEmail()).ifPresent(user -> {
      throw new EmailTakenException(registerData.getEmail());
    });
    userRepository.findByUsername(registerData.getUsername()).ifPresent(user -> {
      throw new UsernameTakenException(registerData.getUsername());
    });
    UserEntity user = new UserEntity();
    user.setUsername(registerData.getUsername());
    user.setPassword(passwordEncoder.encode(registerData.getPassword()));
    user.setFirstName(registerData.getFirstName());
    user.setLastName(registerData.getLastName());
    user.setEmail(registerData.getEmail());
    return userRepository.save(user);
  }

  @Transactional
  public void grantPermission(GrantPermissionDto grantPermissionDto) {
    var repository = grantPermissionMapping.getRepository(grantPermissionDto.getObjectType());
    var objectClass = grantPermissionMapping.getObjectClass(grantPermissionDto.getObjectType());
    Permission permission =
        grantPermissionMapping.getPermission(grantPermissionDto.getPermission());
    Boolean objectExists = repository.existsById(grantPermissionDto.getObjectId());
    if (!objectExists) {
      throw new IllegalArgumentException("Invalid object ID");
    }
    Boolean userExists = userRepository.existsByUsername(grantPermissionDto.getUsername());
    if (!userExists) {
      throw new IllegalArgumentException("Invalid username");
    }

    ObjectIdentity oi = new ObjectIdentityImpl(objectClass, grantPermissionDto.getObjectId());
    Sid sid = new PrincipalSid(grantPermissionDto.getUsername());
    MutableAcl acl = null;
    try {
      acl = (MutableAcl) aclService.readAclById(oi);
    } catch (NotFoundException nfe) {
      acl = aclService.createAcl(oi);
    }

    // Now grant some permissions via an access control entry (ACE)
    acl.insertAce(acl.getEntries().size(), permission, sid, true);
    aclService.updateAcl(acl);
  }

}
