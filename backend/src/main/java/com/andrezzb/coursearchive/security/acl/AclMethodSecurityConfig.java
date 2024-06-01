package com.andrezzb.coursearchive.security.acl;

import javax.sql.DataSource;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableMethodSecurity
public class AclMethodSecurityConfig {

  @Bean
  PermissionCacheOptimizer permissionCacheOptimizer(AclService aclService) {
    return new AclPermissionCacheOptimizer(aclService);
  }

  @Bean
  static MethodSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator,
                                                           PermissionCacheOptimizer permissionCacheOptimizer, RoleHierarchy roleHierarchy) {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(permissionEvaluator);
    expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer);
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }

  @Bean
  PermissionEvaluator permissionEvaluator(AclService aclService) {
    return new AclPermissionEvaluator(aclService);
  }


  @Bean
  AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }

  @Bean
  PermissionGrantingStrategy permissionGrantingStrategy() {
    return new AclPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  SpringCacheBasedAclCache aclCache(PermissionGrantingStrategy permissionGrantingStrategy,
                                    AclAuthorizationStrategy aclAuthorizationStrategy) {
    final ConcurrentMapCache cache = new ConcurrentMapCache("acl_cache");
    return new SpringCacheBasedAclCache(cache, permissionGrantingStrategy,
      aclAuthorizationStrategy);
  }

  @Bean
  MutableAclService aclService(DataSource dataSource, LookupStrategy lookupStrategy, SpringCacheBasedAclCache aclCache) {
    var aclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    aclService.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
    aclService.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
    return aclService;
  }

  @Bean
  LookupStrategy lookupStrategy(DataSource dataSource, SpringCacheBasedAclCache aclCache,
                                AclAuthorizationStrategy aclAuthorizationStrategy, PermissionGrantingStrategy permissionGrantingStrategy) {
    return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, permissionGrantingStrategy);
  }

  @Bean
  static RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
  }
}
