package com.andrezzb.coursearchive.security.services;

import com.andrezzb.coursearchive.course.services.CourseService;
import com.andrezzb.coursearchive.security.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.security.exceptions.UserNotFoundException;
import com.andrezzb.coursearchive.security.models.Role;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.repository.RoleRepository;
import com.andrezzb.coursearchive.security.repository.UserRepository;

import java.util.List;


@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final CourseService courseService;

  public UserService(UserRepository userRepository, RoleRepository roleRepository,
    ModelMapper modelMapper, CourseService courseService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
    this.courseService = courseService;
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public boolean existsByEmail(String email) {
    var userOptional = userRepository.findByEmail(email);
    return userOptional.isPresent();
  }

  public UserEntity findByUsername(String username) {
    return userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
  }

  public UserEntity findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
  }

  public Role findRoleByName(Role.RoleName name) {
    return findRoleByName(name.toString());
  }

  public Role findRoleByName(String name) {
    return roleRepository.findByName(name);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public Page<UserDto> findAllUsersPaged(Pageable p, List<String> filterFields,
    List<Object> filterValueObjs) {
    var users = userRepository.findAllByFilterFieldAndFilterValue(p, filterFields, filterValueObjs);
    return users.map(user -> modelMapper.map(user, UserDto.class));
  }

  public UserEntity addFavoriteCourse(Long courseId, String name) {
    var user = findByUsername(name);
    var course = courseService.findCourseById(courseId);
    boolean updated = user.getFavoriteCourses().add(course);
    if (!updated) {
      return user;
    }
    return userRepository.save(user);
  }

  public UserEntity removeFavoriteCourse(Long courseId, String name) {
    var user = findByUsername(name);
    var course = courseService.findCourseById(courseId);
    boolean updated = user.getFavoriteCourses().remove(course);
    if (!updated) {
      return user;
    }
    return userRepository.save(user);
  }

}
