package com.andrezzb.coursearchive.college.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
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
import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.exceptions.ErrorObject;
import com.andrezzb.coursearchive.security.SecurityConfig;
import com.andrezzb.coursearchive.security.repository.UserRepository;
import com.andrezzb.coursearchive.security.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CollegeController.class)
@Import({SecurityConfig.class, CustomUserDetailsService.class})
@WebAppConfiguration
public class CollegeControllerUnitTest {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CollegeService collegeService;

  @Nested
  @DisplayName("GET api/college/")
  class GetAllColleges {
    private static final String URL = "/api/college/";

    private College college;

    @BeforeEach
    void setUp() {
      college = new College();
      college.setId(1L);
      college.setName("College Name");
    }

    @Test
    @WithMockUser
    void givenNoParams_whenGetAllColleges_thenReturnsListOfPagedCollegesUsingDefaultParamValues()
        throws Exception {
      // Arrange
      when(collegeService.findAllCollegesPaged(any(Pageable.class)))
          .thenReturn(new PageImpl<>(List.of(college)));
      // Act
      mvc.perform(MockMvcRequestBuilders.get(URL))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.content").isArray(),
              jsonPath("$.content[0].id").value(1),
              jsonPath("$.content[0].name").value("College Name"));

      ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
      verify(collegeService).findAllCollegesPaged(pageableCaptor.capture());
      Pageable pageable = pageableCaptor.getValue();
      assertThat(pageable.getPageNumber()).isEqualTo(0);
      assertThat(pageable.getPageSize()).isEqualTo(5);
      assertThat(pageable.getSort().getOrderFor("id").isAscending()).isTrue();
    }

    @Test
    @WithMockUser
    void givenParams_whenGetAllColleges_thenReturnsListOfPagedCollegesUsingParams()
        throws Exception {
      // Arrange
      int page = 1;
      int size = 10;
      String sortDirection = "desc";
      String sortField = "name";

      when(collegeService.findAllCollegesPaged(any(Pageable.class)))
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
      verify(collegeService).findAllCollegesPaged(pageableCaptor.capture());
      Pageable pageable = pageableCaptor.getValue();
      assertThat(pageable.getPageNumber()).isEqualTo(page);
      assertThat(pageable.getPageSize()).isEqualTo(size);
      assertThat(pageable.getSort().getOrderFor(sortField).isDescending()).isTrue();
    }


  }

  @Nested
  @DisplayName("POST api/college/")
  class CreateCollege {
    private static final String URL = "/api/college/";

    @Test
    @WithMockUser
    void givenMissingName_whenCreateCollege_thenReturnsError() throws Exception {
      // Arrange
      CollegeCreateDto collegeCreateDto = CollegeCreateDto.builder()
          .city("City")
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(collegeCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();


      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);
      verify(collegeService, never()).createCollege(any(CollegeCreateDto.class));
      assertThat(responseMap.getErrors()).contains("College name is required");
    }

    @Test
    @WithMockUser
    void givenMissingCity_whenCreateCollege_thenReturnsError() throws Exception {
      // Arrange
      CollegeCreateDto collegeCreateDto = CollegeCreateDto.builder()
          .name("College Name")
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(collegeCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);
      verify(collegeService, never()).createCollege(any(CollegeCreateDto.class));
      assertThat(responseMap.getErrors()).contains("City is required");
    }

    @Test
    @WithMockUser
    void givenMissingPostcode_whenCreateCollege_thenReturnsError() throws Exception {
      // Arrange
      CollegeCreateDto collegeCreateDto = CollegeCreateDto.builder()
          .name("College Name")
          .city("City")
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(collegeCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);
      verify(collegeService, never()).createCollege(any(CollegeCreateDto.class));
      assertThat(responseMap.getErrors()).contains("Postcode is required");
    }

    @Test
    @WithMockUser
    void givenMissingAddress_whenCreateCollege_thenReturnsError() throws Exception {
      // Arrange
      CollegeCreateDto collegeCreateDto = CollegeCreateDto.builder()
          .name("College Name")
          .city("City")
          .postcode(12345)
          .build();

      // Act
      MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(collegeCreateDto)))
          .andExpect(status().isBadRequest()).andReturn();

      // Assert
      String content = result.getResponse().getContentAsString();
      ErrorObject responseMap =
          objectMapper.readValue(content, ErrorObject.class);
      verify(collegeService, never()).createCollege(any(CollegeCreateDto.class));
      assertThat(responseMap.getErrors()).contains("Address is required");
    }

    @Test
    @WithMockUser
    void givenValidCollegeCreateDto_whenCreateCollege_thenReturnsCreatedCollege() throws Exception {
      // Arrange
      CollegeCreateDto collegeCreateDto = CollegeCreateDto.builder()
          .name("College Name")
          .city("City")
          .postcode(12345)
          .address("Address")
          .build();
      College college = new College();
      college.setId(1L);
      modelMapper.map(collegeCreateDto, college);

      when(collegeService.createCollege(any(CollegeCreateDto.class)))
          .thenReturn(college);

      // Act
      mvc.perform(MockMvcRequestBuilders.post(URL)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(college)))
          .andExpectAll(
              status().isCreated(),
              jsonPath("$.id").value(1),
              jsonPath("$.name").value("College Name"));
    }
  }

  @Nested
  @DisplayName("GET api/college/{id}")
  class GetCollegeById {
    private static final String URL = "/api/college/{id}";

    @Test
    @WithMockUser
    void givenValidId_whenGetCollegeById_thenReturns() throws Exception {
      // Arrange
      College college = new College();
      college.setId(1L);
      college.setName("College Name");

      when(collegeService.findCollegeById(1L))
          .thenReturn(college);

      // Act
      mvc.perform(MockMvcRequestBuilders.get(URL, college.getId()))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.id").value(1),
              jsonPath("$.name").value("College Name"));

      verify(collegeService).findCollegeById(1L);
    }
  }

  @Nested
  @DisplayName("PUT api/college/{id}")
  class UpdateCollegeById {
    private static final String URL = "/api/college/{id}";

    @Test
    @WithMockUser
    void givenValidIdAndCollegeUpdateDto_whenUpdateCollegeById_thenReturns() throws Exception {
      // Arrange
      CollegeUpdateDto collegeUpdateDto = CollegeUpdateDto.builder()
          .description("Description")
          .build();
      College college = new College();
      college.setId(1L);
      college.setName("College Name");
      college.setDescription(collegeUpdateDto.getDescription());

      when(collegeService.updateCollege(eq(college.getId()), any(CollegeUpdateDto.class)))
          .thenReturn(college);

      // Act
      mvc.perform(MockMvcRequestBuilders.put(URL, college.getId())
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(collegeUpdateDto)))
          .andExpectAll(
              status().isOk(),
              jsonPath("$.id").value(1),
              jsonPath("$.name").value("College Name"),
              jsonPath("$.description").value("Description"));

      ArgumentCaptor<CollegeUpdateDto> collegeUpdateDtoCaptor =
          ArgumentCaptor.forClass(CollegeUpdateDto.class);
      verify(collegeService).updateCollege(eq(1L), collegeUpdateDtoCaptor.capture());
      assertThat(collegeUpdateDtoCaptor.getValue().getDescription()).isEqualTo("Description");
    }
  }
}
