package com.andrezzb.coursearchive.material.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.andrezzb.coursearchive.material.repository.MaterialGroupRepository;

@ExtendWith(MockitoExtension.class)
public class MaterialGroupServiceUnitTest {
  @Mock
  private MaterialGroupRepository materialGroupRepository;

  @InjectMocks
  private MaterialGroupService materialGroupService;

  @Nested
  @DisplayName("Material group order")
  class MaterialGroupOrder {

    @Test
    public void givenOldOrder_whenNewOrderIsNull_thenShouldReturnOldOrder() {
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(6L));

      assertEquals((short) 3, materialGroupService.getNewDisplayOrder(1L, (short) 3, null));
    }

    @Test
    public void givenNewOrderHigherThanMax_whenGettingNewDisplayOrder_thenShouldReturnMaxPlusOne() {
      when(materialGroupRepository.findMaxOrder(1L)).thenReturn(Optional.of((short) 5));
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(6L));

      assertEquals((short) 6, materialGroupService.getNewDisplayOrder(1L, null, (short) 7));
    }

    @Test
    public void givenNewOrderLowerThanMax_whenGettingNewDisplayOrder_thenShouldReturnNewOrderAndCallIncrementDisplayOrder() {
      when(materialGroupRepository.findMaxOrder(1L)).thenReturn(Optional.of((short) 5));
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(6L));

      assertEquals((short) 2, materialGroupService.getNewDisplayOrder(1L, null, (short) 2));
      verify(materialGroupRepository).incrementDisplayOrder(1L, (short) 2);
    }

    @Test
    public void givenGroupCountIsZero_whenGettingNewDisplayOrder_thenShouldReturnZero() {
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(0L));

      assertEquals((short) 0, materialGroupService.getNewDisplayOrder(1L, null, (short) 5));
    }

    @Test
    public void givenOldOrderIsNull_whenNewOrderIsNull_thenShouldReturnMaxOrderPlusOne() {
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(6L));
      when(materialGroupRepository.findMaxOrder(1L)).thenReturn(Optional.of((short) 5));

      assertEquals((short) 6, materialGroupService.getNewDisplayOrder(1L, null, null));
    }

    @Test
    public void givenMaxOrderIsMinusOne_whenGettingNewDisplayOrder_thenShouldNotCallIncrementDisplayOrder() {
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(1L));

      assertEquals((short) 0, materialGroupService.getNewDisplayOrder(1L, null, (short) 0));
      verify(materialGroupRepository, never()).incrementDisplayOrder(anyLong(), anyShort());
    }

    @Test
    public void givenGroupCountIsOne_whenGettingNewDisplayOrder_thenShouldReturnZero() {
      when(materialGroupRepository.countByCourseYearId(1L)).thenReturn(Optional.of(1L));

      assertEquals((short) 0, materialGroupService.getNewDisplayOrder(1L, null, (short) 5));
    }
  }
}
