package com.andrezzb.coursearchive.security.services;

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
    // Now grant some permissions via an access control entry (ACE)
    acl.insertAce(acl.getEntries().size(), permission, sid, true);
    aclService.updateAcl(acl);
  }


  private void setParent(MutableAcl acl, AclSecured object) {
    acl.setEntriesInheriting(object.isInheriting());
    if (object.getParent() == null) {
      return;
    }
    Acl parentAcl = aclService.readAclById(new ObjectIdentityImpl(object.getParent()));
    acl.setParent(parentAcl);
  }
}
