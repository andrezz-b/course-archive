package com.andrezzb.coursearchive.security.services;

import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.dto.PermissionData;
import org.hibernate.Hibernate;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.security.models.AclSecured;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.domain.PrincipalSid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AclUtilService {

  private final MutableAclService aclService;

  public AclUtilService(MutableAclService aclService) {
    this.aclService = aclService;
  }

  @Transactional
  public void grantPermission(AclSecured object, String username, Permission permission) {
    grantPermission(object, new PrincipalSid(username), permission);
  }

  @Transactional
  public void grantPermission(AclSecured object, Sid sid, Permission permission) {
    ObjectIdentity oi = new ObjectIdentityImpl(object);
    MutableAcl acl;
    try {
      acl = (MutableAcl) aclService.readAclById(oi);
    } catch (NotFoundException nfe) {
      acl = aclService.createAcl(oi);
    }

    this.setParent(acl, object);
    acl.insertAce(acl.getEntries().size(), permission, sid, true);
    aclService.updateAcl(acl);
  }


  private void setParent(MutableAcl acl, AclSecured object) {
    acl.setEntriesInheriting(object.isInheriting());
    AclSecured parent = (AclSecured) Hibernate.unproxy(object.getParent());
    if (parent == null) {
      return;
    }
    Acl parentAcl =
      aclService.readAclById(new ObjectIdentityImpl(parent.getClass(), parent.getId()));
    acl.setParent(parentAcl);
  }

  @Transactional
  public void revokePermission(AclSecured object, String username, Permission permission) {
    revokePermission(object, new PrincipalSid(username), permission);
  }

  @Transactional
  public void revokePermission(AclSecured object, Sid sid, Permission permission) {
    ObjectIdentity oi = new ObjectIdentityImpl(object);
    MutableAcl acl;
    try {
      acl = (MutableAcl) aclService.readAclById(oi);
    } catch (NotFoundException e) {
      // No ACL found, nothing to revoke
      return;
    }
    int aceIndex = -1;
    for (int i = 0; i < acl.getEntries().size(); i++) {
      var ace = acl.getEntries().get(i);
      if (ace.getSid().equals(sid) && ace.getPermission().equals(permission)) {
        aceIndex = i;
        break;
      }
    }
    if (aceIndex != -1) {
      acl.deleteAce(aceIndex);
      aclService.updateAcl(acl);
    }
  }

  public Map<AclPermission.PermissionType, PermissionData> getPermissionData(
    Class<? extends AclSecured> clazz, Long id, String username) {
    ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);
    Sid sid = new PrincipalSid(username);
    return getPermissionData(oi, sid);
  }

  public Map<AclPermission.PermissionType, PermissionData> getPermissionData(ObjectIdentity oi,
    Sid sid) {
    var aclMap = aclService.readAclsById(List.of(oi), List.of(sid));
    var objectAcl = aclMap.get(oi);
    Map<AclPermission.PermissionType, PermissionData> permissions = new HashMap<>();
    for (Map.Entry<AclPermission.PermissionType, Permission> entry : AclPermission.permissionMap.entrySet()) {
      AclPermission.PermissionType permissionType = entry.getKey();
      Permission permission = entry.getValue();
      boolean result = false;
      boolean grantedByParent = false;
      try {
        result = objectAcl.isGranted(List.of(permission), List.of(sid), false);
        if (result) {
          grantedByParent = isGrantedByParent(objectAcl, sid, permission);
        }
      } catch (NotFoundException ignored) {
      }
      permissions.put(permissionType,
        PermissionData.builder().granted(result).grantedByParent(grantedByParent).build());
    }
    return permissions;
  }

  private boolean isGrantedByParent(Acl objectAcl, Sid sid, Permission permission) {
    for (var ace : objectAcl.getEntries()) {
      if (ace.getSid().equals(sid) && (ace.getPermission()
        .getMask() & permission.getMask()) >= permission.getMask()) {
        return false;
      }
    }
    return true;
  }
}
