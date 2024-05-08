package com.andrezzb.coursearchive.program.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import java.util.List;

import com.andrezzb.coursearchive.exceptions.ErrorObject;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.dto.ProgramDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.services.ProgramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.andrezzb.coursearchive.security.SecurityConfig;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import com.andrezzb.coursearchive.security.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;

@WebMvcTest(ProgramController.class)
@Import({SecurityConfig.class, CustomUserDetailsService.class})
@WebAppConfiguration
public class ProgramControllerUnitTest {
  @MockBean
  private UserRepository userRepository;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private ProgramService programService;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mvc;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Nested
  @DisplayName("GET api/program/")
  class GetAllPrograms {
    private static final String URL = "/api/program/";

    private ProgramDto program;

    @BeforeEach
    void setUpProgram() {
      program = new ProgramDto();
      program.setId(1L);
      program.setName("Program name");
      assertThat(programService).isNotNull();
    }

    @Test
    @WithMockUser
    void givenNoParams_whenGetAllPrograms_thenReturnsListOfPagedProgramsUsingDefaultParams()
        throws Exception {
      when(programService.findAllProgramsPaged(any(Pageable.class), any(), any(), any()))
          .thenReturn(new PageImpl<>(List.of(program)));

      mvc.perform(MockMvcRequestBuilders.get(URL))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.content").isArray(),
              jsonPath("$.content[0].id").value(program.getId()),
              jsonPath("$.content[0].name").value(program.getName()));

      ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
      verify(programService).findAllProgramsPaged(pageableCaptor.capture(), any(), any(), any());
      Pageable pageable = pageableCaptor.getValue();
      assertThat(pageable.getPageNumber()).isEqualTo(0);
      assertThat(pageable.getPageSize()).isEqualTo(5);
      assertThat(pageable.getSort().getOrderFor("id").isAscending()).isTrue();
    }

    @Test
    @WithMockUser
    void givenParams_whenGetAllPrograms_thenReturnsListOfPagedProgramsUsingParams()
        throws Exception {
      // Arrange
      int page = 1;
      int size = 10;
      String sortDirection = "desc";
      String sortField = "name";

      when(programService.findAllProgramsPaged(any(Pageable.class), any(), any(), any()))
          .thenReturn(new PageImpl<>(List.of()));

      // Act
      mvc.perform(MockMvcRequestBuilders.get(URL)
          .param("page", String.valueOf(page))
          .param("size", String.valueOf(size))
          .param("sortDirection", sortDirection)
          .param("sortField", sortField))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.content").isArray(),
              jsonPath("$.content[0]").doesNotExist());

      ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
      verify(programService).findAllProgramsPaged(pageableCaptor.capture(), any(), any(), any());
      Pageable pageable = pageableCaptor.getValue();
      assertThat(pageable.getPageNumber()).isEqualTo(page);
      assertThat(pageable.getPageSize()).isEqualTo(size);
      assertThat(pageable.getSort().getOrderFor(sortField).isDescending()).isTrue();
    }
  }

  @Nested
  @DisplayName("POST api/program/")
  class CreateProgram {
    private static final String URL = "/api/program/";

    @Test
    @WithMockUser
    void givenMissingName_whenCreateProgram_thenReturnsError() throws Exception {
      // Arrange
      ProgramCreateDto programCreateDto = ProgramCreateDto.builder()
          .degreeTitle("Title")
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(programCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap = objectMapper.readValue(content, ErrorObject.class);
      verify(programService, never()).createProgram(any(ProgramCreateDto.class));
      assertThat(responseMap.getErrors()).contains("Program name is required");
    }

    @Test
    @WithMockUser
    void givenMissingCollegeId_whenCreateProgram_thenReturnsError() throws Exception {
      // Arrange
      ProgramCreateDto programCreateDto = ProgramCreateDto.builder()
          .name("program Name")
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(programCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap = objectMapper.readValue(content, ErrorObject.class);
      verify(programService, never()).createProgram(any(ProgramCreateDto.class));
      assertThat(responseMap.getErrors()).contains("College id is required");
    }

    @Test
    @WithMockUser
    void givenDurationLessThan1Year_whenCreateProgram_thenReturnsError() throws Exception {
      // Arrange
      ProgramCreateDto programCreateDto = ProgramCreateDto.builder()
          .name("program Name")
          .duration((short) -1)
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(programCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap = objectMapper.readValue(content, ErrorObject.class);
      verify(programService, never()).createProgram(any(ProgramCreateDto.class));
      assertThat(responseMap.getErrors()).contains("Duration must be at least 1 year");
    }

    @Test
    @WithMockUser
    void givenValidProgramData_whenCreateProgram_thenReturnsProgram() throws Exception {
      ProgramCreateDto programCreateDto = ProgramCreateDto.builder()
          .name("Program name")
          .collegeId(1L)
          .build();

      var program = new ProgramDto();
      program.setId(1l);
      program.setName(programCreateDto.getName());
      when(programService.createProgram(any(ProgramCreateDto.class)))
          .thenReturn(program);

      mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(programCreateDto)))
          .andExpectAll(
              status().isCreated(),
              jsonPath("$.id").value(program.getId()),
              jsonPath("$.name").value(program.getName()));

    }
  }

  @Nested
  @DisplayName("GET api/program/{id}")
  class GetProgramById {
    private static final String URL = "/api/program/{id}";

    @Test
    @WithMockUser
    void givenValidId_whenGetProgramById_thenReturnsProgram() throws Exception {
      var program = new ProgramDto();
      program.setId(1L);
      program.setName("Program name");

      when(programService.findProgramById(1L))
          .thenReturn(program);

      mvc.perform(MockMvcRequestBuilders.get(URL, program.getId()))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.id").value(program.getId()),
              jsonPath("$.name").value(program.getName()));

      verify(programService).findProgramById(1L);
    }
  }

  @Nested
  @DisplayName("PUT api/program/{id}")
  class UpdateProgramById {
    private static final String URL = "/api/program/{id}";

    @Test
    @WithMockUser
    void givenValidIdAndProgramUpdateDto_whenUpdateProgramById_thenReturnsUpdated()
        throws Exception {
      ProgramUpdateDto programUpdateDto = ProgramUpdateDto.builder()
          .description("My new description")
          .build();

      var program = new ProgramDto();
      program.setId(1l);
      program.setDescription(programUpdateDto.getDescription());

      when(programService.updateProgram(eq(1L), any(ProgramUpdateDto.class)))
          .thenReturn(program);

      mvc.perform(MockMvcRequestBuilders.put(URL, program.getId())
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(programUpdateDto)))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.id").value(program.getId()),
              jsonPath("$.description").value(program.getDescription()));

      ArgumentCaptor<ProgramUpdateDto> programUpdateDtoCaptor =
          ArgumentCaptor.forClass(ProgramUpdateDto.class);
      verify(programService).updateProgram(eq(1L), programUpdateDtoCaptor.capture());
      assertThat(programUpdateDtoCaptor.getValue().getDescription())
          .isEqualTo(programUpdateDto.getDescription());
    }
  }

  @Nested
  @DisplayName("DELETE api/program/{id}")
  class DeleteProgramById {
    private static final String URL = "/api/program/{id}";

    @Test
    @WithMockUser
    void givenValidId_whenDeleteProgramById_thenReturnsOk() throws Exception {
      mvc.perform(MockMvcRequestBuilders.delete(URL, 1L))
          .andExpect(status().isNoContent());

      verify(programService).deleteProgramById(1L);
    }
  }
}
