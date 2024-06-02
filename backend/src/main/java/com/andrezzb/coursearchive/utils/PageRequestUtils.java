package com.andrezzb.coursearchive.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageRequestUtils {
  public static Pageable createPageRequest(int page, int size, List<String> sortFields,
    List<String> sortDirections) {
    if (sortFields.size() != sortDirections.size()) {
      throw new IllegalArgumentException("Sort fields and directions must be of the same size");
    }

    List<Sort.Order> orders = new ArrayList<>();
    for (int i = 0; i < sortFields.size(); i++) {
      orders.add(
        new Sort.Order(Sort.Direction.fromString(sortDirections.get(i)), sortFields.get(i)));
    }

    Sort sort = Sort.by(orders);
    return PageRequest.of(page, size, sort);
  }

  public static Pageable createPageRequest(int page, int size, String sortField,
    String sortDirection) {
    return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
  }
}
