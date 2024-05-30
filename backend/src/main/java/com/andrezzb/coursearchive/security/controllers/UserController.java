package com.andrezzb.coursearchive.security.controllers;

import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.security.dto.UserDto;
import com.andrezzb.coursearchive.security.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

  private final UserService userService;
  private final ModelMapper modelMapper;

  public UserController(UserService userService, ModelMapper modelMapper) {
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/current")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    final var user = userService.findByUsername(authentication.getName());
    return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
  }

  @GetMapping("/")
  public ResponseEntity<Page<UserDto>> getAllUsers(
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "10") int size,
    @ValidEnum(enumClazz = Sort.Direction.class,
      ignoreCase = true) @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = UserEntity.SortField.class) @RequestParam(
      defaultValue = "id") List<String> sortField,
    @ValidEnum(enumClazz = UserEntity.FilterField.class, required = false) @RequestParam(
      required = false) List<String> filterField,
    @RequestParam(required = false) List<String> filterValue) {
    List<Object> filterValueObjs = FilterValueMapper.mapFilterValue(UserEntity.FilterField.class, filterField, filterValue);
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    var usersPaged = userService.findAllUsersPaged(p, filterField, filterValueObjs);
    return ResponseEntity.ok(usersPaged);
  }


  private Pageable createPageRequest(int page, int size, List<String> sortFields, List<String> sortDirections) {
    if (sortFields.size() != sortDirections.size()) {
      throw new IllegalArgumentException("Sort fields and directions must be of the same size");
    }

    List<Sort.Order> orders = new ArrayList<>();
    for (int i = 0; i < sortFields.size(); i++) {
      orders.add(new Sort.Order(Sort.Direction.fromString(sortDirections.get(i)), sortFields.get(i)));
    }

    Sort sort = Sort.by(orders);
    return PageRequest.of(page, size, sort);
  }

}
