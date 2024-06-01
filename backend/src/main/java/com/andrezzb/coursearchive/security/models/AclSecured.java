package com.andrezzb.coursearchive.security.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AclSecured {
  Long getId();
  @JsonIgnore
  AclSecured getParent();
  @JsonIgnore
  default Boolean isInheriting() {
    return true;
  }
}
