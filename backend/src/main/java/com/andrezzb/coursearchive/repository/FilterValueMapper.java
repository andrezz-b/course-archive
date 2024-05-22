package com.andrezzb.coursearchive.repository;

public interface FilterValueMapper {
  default Object map(String filterValue) {
    return filterValue;
  }

    static <E extends Enum<E> & FilterValueMapper> Object mapFilterValue(Class<E> enumClass, String filterField, String filterValue) {
    if (filterField == null || filterValue == null || filterValue.isEmpty()) {
        return null;
    }
    E enumValue = Enum.valueOf(enumClass, filterField);
    return enumValue.map(filterValue);
}
}
