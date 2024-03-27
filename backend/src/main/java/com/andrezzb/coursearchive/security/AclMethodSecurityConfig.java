package com.andrezzb.coursearchive.security;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableMethodSecurity
public class AclMethodSecurityConfig {

  @Autowired
  private DataSource dataSource;


  @Bean
  PermissionCacheOptimizer permissionCacheOptimizer() {
    return new AclPermissionCacheOptimizer(aclService());
  }

  @Bean
  MethodSecurityExpressionHandler expressionHandler() {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(permissionEvaluator());
    expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer());
    return expressionHandler;
  }

  @Bean
  PermissionEvaluator permissionEvaluator() {
    return new AclPermissionEvaluator(aclService());
  }


  @Bean
  AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ACL_ADMIN"));
  }

  @Bean
  PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  SpringCacheBasedAclCache aclCache() {
    final ConcurrentMapCache cache = new ConcurrentMapCache("acl_cache");
    return new SpringCacheBasedAclCache(cache, permissionGrantingStrategy(),
        aclAuthorizationStrategy());
  }

  @Bean
  AclService aclService() {
    var aclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
    aclService.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id')");
    aclService.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id')))");
    return aclService;
  }

  @Bean
  LookupStrategy lookupStrategy() {
    return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(),
        new ConsoleAuditLogger());
  }
}
