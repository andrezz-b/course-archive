package com.andrezzb.coursearchive.security.acl;

import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;

public class AclPermissionGrantingStrategy extends DefaultPermissionGrantingStrategy {

  public AclPermissionGrantingStrategy(AuditLogger auditLogger) {
    super(auditLogger);
  }

  @Override
  protected boolean isGranted(AccessControlEntry ace, Permission p) {
    if (ace.isGranting() && p.getMask() != 0) {
      return (ace.getPermission().getMask() & p.getMask()) >= p.getMask();
    } else {
      return ace.getPermission().getMask() == p.getMask();
    }
  }

}
