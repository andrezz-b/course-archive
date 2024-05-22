package com.andrezzb.coursearchive.security.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AclSecured {
  Long getId();
  @JsonIgnore
  Object getParent();
  @JsonIgnore
  default Boolean isInheriting() {
    return true;
  }
}
