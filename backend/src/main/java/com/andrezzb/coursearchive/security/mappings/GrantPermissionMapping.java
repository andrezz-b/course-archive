package com.andrezzb.coursearchive.security.mappings;

import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.repository.CourseRepository;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.repository.ProgramRepository;
import com.andrezzb.coursearchive.security.models.AclSecured;


@Component
public class GrantPermissionMapping {

  private final ApplicationContext applicationContext;

  private final Map<String, Class<? extends JpaRepository<? extends AclSecured, Long>>> repositoryMap =
      Map.of(
          "college", CollegeRepository.class,
          "program", ProgramRepository.class,
          "course", CourseRepository.class);

  private final Map<String, Class<?>> objectMap = Map.of(
      "college", College.class,
      "program", Program.class,
      "course", Course.class);

  private final Map<String, Permission> permissionMap = Map.of(
      "read", BasePermission.READ,
      "write", BasePermission.WRITE,
      "delete", BasePermission.DELETE,
      "create", BasePermission.CREATE,
      "admin", BasePermission.ADMINISTRATION);


  public GrantPermissionMapping(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public JpaRepository<?, Long> getRepository(String objectType) {
    var repositoryClass = repositoryMap.get(objectType);
    if (repositoryClass == null) {
      throw new IllegalArgumentException("Invalid object type");
    }
    return applicationContext.getBean(repositoryClass);
  }

  public Class<?> getObjectClass(String objectType) {
    var objectClass = objectMap.get(objectType);
    if (objectClass == null) {
      throw new IllegalArgumentException("Invalid object type");
    }
    return objectClass;
  }

  public Permission getPermission(String permission) {
    var permissionObject = permissionMap.get(permission);
    if (permissionObject == null) {
      throw new IllegalArgumentException("Invalid permission");
    }
    return permissionObject;
  }
}
