package com.andrezzb.coursearchive.repository;

import java.util.ArrayList;
import java.util.List;

public interface FilterValueMapper {
  default Object map(String filterValue) {
    return filterValue;
  }

  static <E extends Enum<E> & FilterValueMapper> List<Object> mapFilterValue(Class<E> enumClass, List<String> filterField, List<String> filterValue) {
    if (filterField == null || filterValue == null || filterValue.isEmpty() || filterField.isEmpty() || filterField.size() != filterValue.size()) {
      return null;
    }
    List<Object> mappedValues = new ArrayList<>();
    for (int i = 0; i < filterField.size(); i++) {
      Object value = mapFilterValue(enumClass, filterField.get(i), filterValue.get(i));
      mappedValues.add(value);
    }
    return mappedValues;
  }

    static <E extends Enum<E> & FilterValueMapper> Object mapFilterValue(Class<E> enumClass, String filterField, String filterValue) {
    if (filterField == null || filterValue == null || filterValue.isEmpty()) {
        return null;
    }
    E enumValue = Enum.valueOf(enumClass, filterField);
    return enumValue.map(filterValue);
}
}
