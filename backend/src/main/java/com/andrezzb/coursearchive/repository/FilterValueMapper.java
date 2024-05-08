package com.andrezzb.coursearchive.repository;

public interface FilterValueMapper {
  default Object map(String filterValue) {
    return filterValue;
  };

  public static <E extends Enum<E> & FilterValueMapper> Object mapFilterValue(Class<E> enumClass, String filterField, String filterValue) {
    if (filterField == null || filterValue == null || filterValue.length() == 0) {
        return null;
    }
    E enumValue = Enum.valueOf(enumClass, filterField);
    return enumValue.map(filterValue);
}
}
