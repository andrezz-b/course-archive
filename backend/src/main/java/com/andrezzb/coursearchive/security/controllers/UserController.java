package com.andrezzb.coursearchive.security.controllers;

import com.andrezzb.coursearchive.mappings.ApplicationObjectType;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.andrezzb.coursearchive.security.dto.UserDto;
import com.andrezzb.coursearchive.security.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.andrezzb.coursearchive.utils.PageRequestUtils.createPageRequest;

@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

  private final UserService userService;
  private final ModelMapper modelMapper;
  private final AclService aclService;

  public UserController(UserService userService, ModelMapper modelMapper, AclService aclService) {
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.aclService = aclService;
  }

  @GetMapping("/current")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    final var user = userService.findByUsername(authentication.getName());
    return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
  }

  @GetMapping("/")
  public ResponseEntity<Page<UserDto>> getAllUsers(
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "10") int size,
    @ValidEnum(enumClazz = Sort.Direction.class,
      ignoreCase = true) @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = UserEntity.SortField.class) @RequestParam(
      defaultValue = "id") List<String> sortField,
    @ValidEnum(enumClazz = UserEntity.FilterField.class, required = false) @RequestParam(
      required = false) List<String> filterField,
    @RequestParam(required = false) List<String> filterValue) {
    List<Object> filterValueObjs = FilterValueMapper.mapFilterValue(UserEntity.FilterField.class, filterField, filterValue);
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    var usersPaged = userService.findAllUsersPaged(p, filterField, filterValueObjs);
    return ResponseEntity.ok(usersPaged);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    final var user = userService.findById(id);
    return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
  }

  @Data
  public static class AclData {
    private String objectType;
    private Long objectId;
    private String username;
  }

  @PostMapping("/acl")
  private ResponseEntity<Map<AclPermission.PermissionType, Boolean>> readAclData(@RequestBody AclData aclData) {
    ApplicationObjectType objectType = ApplicationObjectType.fromString(aclData.getObjectType());
    ObjectIdentityImpl oi = new ObjectIdentityImpl(objectType.getObjectClass(), aclData.getObjectId());
    PrincipalSid sid = new PrincipalSid(aclData.getUsername());
    var aclMap = aclService.readAclsById(List.of(oi), List.of(sid));
    var objectAcl = aclMap.get(oi);

      Map<AclPermission.PermissionType, Boolean> permissions = new HashMap<>();
    for (Map.Entry<AclPermission.PermissionType, Permission> entry : AclPermission.permissionMap.entrySet()) {
      AclPermission.PermissionType permissionType = entry.getKey();
      Permission permission = entry.getValue();
      boolean result = false;
      Boolean grantedByParent = null;
      try {
        result = objectAcl.isGranted(List.of(permission), List.of(sid), false);
        for (var ace : objectAcl.getEntries()) {
          if (ace.getSid().equals(sid) && (ace.getPermission().getMask() & permission.getMask()) >= permission.getMask()) {
            grantedByParent = false;
            break;
          }
        }
        if (result && grantedByParent == null) {
          grantedByParent = true;
        }
      } catch (NotFoundException ignored) {
      }
      permissions.put(permissionType, result);
      log.info("Permission {} is granted: {}, granted by parent: {}", permissionType, result, grantedByParent);
    }

    return ResponseEntity.ok(permissions);
  }

}
