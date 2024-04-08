package com.andrezzb.coursearchive.security.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AclSecured {
  public Long getId();
  @JsonIgnore
  public Object getParent();
  @JsonIgnore
  default public Boolean isInheriting() {
    return true;
  }
}
