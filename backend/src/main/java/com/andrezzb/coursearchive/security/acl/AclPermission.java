package com.andrezzb.coursearchive.security.acl;

import org.springframework.security.acls.domain.AbstractPermission;
import org.springframework.security.acls.model.Permission;

import java.util.Map;

public class AclPermission extends AbstractPermission {
  public static final Permission READ = new AclPermission(1, 'R'); // 1

  public static final Permission WRITE = new AclPermission((1 << 1 | READ.getMask()), 'W'); // 3

  public static final Permission CREATE = new AclPermission(1 << 2 | WRITE.getMask(), 'C'); // 7

  public static final Permission DELETE = new AclPermission(1 << 3 | CREATE.getMask(), 'D'); // 15

  public static final Permission ADMINISTRATION = new AclPermission(1 << 4 | DELETE.getMask(), 'A'); // 31

  protected AclPermission(int mask) {
    super(mask);
  }

  protected AclPermission(int mask, char code) {
    super(mask, code);
  }

  public enum PermissionType {
    READ, WRITE, CREATE, DELETE, ADMINISTRATION
  }

  private static final Map<PermissionType, Permission> permissionMap = Map.of(
    PermissionType.READ, AclPermission.READ,
    PermissionType.WRITE, AclPermission.WRITE,
    PermissionType.DELETE, AclPermission.DELETE,
    PermissionType.CREATE, AclPermission.CREATE,
    PermissionType.ADMINISTRATION, AclPermission.ADMINISTRATION);

  public static Permission fromString(String permission) {
    return permissionMap.get(PermissionType.valueOf(permission.toUpperCase()));
  }
}
