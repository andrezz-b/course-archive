package com.andrezzb.coursearchive.college.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.acls.domain.BasePermission;
import com.andrezzb.coursearchive.college.dto.CollegeCreateDto;
import com.andrezzb.coursearchive.college.dto.CollegeUpdateDto;
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import com.andrezzb.coursearchive.config.GlobalConfig;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class CollegeServiceUnitTest {

  @Spy
  private ModelMapper modelMapper = new GlobalConfig().modelMapper();

  @Mock
  private CollegeRepository collegeRepository;

  @Mock
  private AclUtilService aclUtilService;

  @InjectMocks
  private CollegeService collegeService;

  @Test
  void givenMissingId_whenFindById_thenThrowsException() {
    Long collegeId = 1L;
    when(collegeRepository.findById(collegeId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> collegeService.findCollegeById(collegeId))
        .isInstanceOf(CollegeNotFoundException.class);
  }

  @Nested
  @DisplayName("Create college")
  class CreateCollege {

    @BeforeEach
    public void setUp() {
      // Create a dummy Authentication
      Authentication authentication =
          new UsernamePasswordAuthenticationToken("dummyUsername", "dummyPassword");

      // Create a SecurityContext
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authentication);

      // Set the SecurityContextHolder to use the SecurityContext
      SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void givenValidCollege_whenCreateCollege_thenCollegeCreated() {
      // Arrange
      CollegeCreateDto collegeDto = CollegeCreateDto.builder()
          .name("Test College")
          .acronym("TC")
          .address("Test Address")
          .city("Test City")
          .postcode(1234)
          .website("testsite")
          .description("Test Description")
          .build();

      when(collegeRepository.save(any(College.class))).thenReturn(null);

      // Act
      collegeService.createCollege(collegeDto);
      // Assert
      ArgumentCaptor<College> collegeCaptor = ArgumentCaptor.forClass(College.class);
      verify(collegeRepository).save(collegeCaptor.capture());
      verify(aclUtilService).grantPermission(eq(null), any(String.class),
          eq(BasePermission.ADMINISTRATION));

      assertThat(collegeCaptor.getValue()).isNotNull();
      assertThat(collegeCaptor.getValue().getName()).isEqualTo(collegeDto.getName());
      assertThat(collegeCaptor.getValue().getAcronym()).isEqualTo(collegeDto.getAcronym());
      assertThat(collegeCaptor.getValue().getAddress()).isEqualTo(collegeDto.getAddress());
      assertThat(collegeCaptor.getValue().getCity()).isEqualTo(collegeDto.getCity());
      assertThat(collegeCaptor.getValue().getPostcode()).isEqualTo(collegeDto.getPostcode());
      assertThat(collegeCaptor.getValue().getWebsite()).isEqualTo(collegeDto.getWebsite());
      assertThat(collegeCaptor.getValue().getDescription()).isEqualTo(collegeDto.getDescription());
    }

  }

  @Nested
  @DisplayName("Update college")
  class UpdateCollege {

    @Test
    void givenMissingId_whenUpdateCollege_thenThrowsException() {
      Long collegeId = 1L;
      CollegeUpdateDto collegeDto = CollegeUpdateDto.builder()
          .acronym("TC")
          .address("Test Address")
          .city("Test City")
          .postcode(1234)
          .website("testsite")
          .description("Test Description")
          .build();

      when(collegeRepository.findById(collegeId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> collegeService.updateCollege(collegeId, collegeDto))
          .isInstanceOf(CollegeNotFoundException.class);
    }

    @Test
    void givenOnlyDescription_whenUpdateCollege_thenOnlyDescriptionChanged() {
      Long collegeId = 1L;
      College college = new College();
      college.setId(collegeId);
      college.setName("Test College");
      college.setAcronym("TC");
      college.setAddress("Test Address");
      college.setCity("Test City");
      college.setPostcode(1234);
      college.setWebsite("testsite");
      college.setDescription("Test Description");

      CollegeUpdateDto collegeDto = CollegeUpdateDto.builder()
          .description("Test Description Updated")
          .build();

      when(collegeRepository.findById(collegeId)).thenReturn(Optional.of(college));
      when(collegeRepository.save(any(College.class))).thenReturn(null);

      collegeService.updateCollege(collegeId, collegeDto);
      ArgumentCaptor<College> collegeCaptor = ArgumentCaptor.forClass(College.class);
      verify(collegeRepository).save(collegeCaptor.capture());

      assertThat(collegeCaptor.getValue()).isNotNull();
      assertThat(collegeCaptor.getValue().getId()).isEqualTo(collegeId);
      assertThat(collegeCaptor.getValue().getAcronym()).isEqualTo(college.getAcronym());
      assertThat(collegeCaptor.getValue().getAddress()).isEqualTo(college.getAddress());
      assertThat(collegeCaptor.getValue().getCity()).isEqualTo(college.getCity());
      assertThat(collegeCaptor.getValue().getPostcode()).isEqualTo(college.getPostcode());
      assertThat(collegeCaptor.getValue().getWebsite()).isEqualTo(college.getWebsite());
      // Updated field
      assertThat(collegeCaptor.getValue().getDescription()).isEqualTo(collegeDto.getDescription());
    }

    @Test
    void givenMultipleFields_whenUpdateCollege_thenOnlyGivenFieldsUpdated() {

      Long collegeId = 1L;
      College college = new College();
      college.setId(collegeId);
      college.setName("Test College");
      college.setAcronym("TC");
      college.setAddress("Test Address");
      college.setCity("Test City");
      college.setPostcode(1234);
      college.setWebsite("testsite");
      college.setDescription("Test Description");

      CollegeUpdateDto collegeDto = CollegeUpdateDto.builder()
          .acronym("TC Updated")
          .address("Test Address Updated")
          .website("testsite Updated")
          .description("Test Description Updated")
          .build();

      when(collegeRepository.findById(collegeId)).thenReturn(Optional.of(college));
      when(collegeRepository.save(any(College.class))).thenReturn(null);

      collegeService.updateCollege(collegeId, collegeDto);
      ArgumentCaptor<College> collegeCaptor = ArgumentCaptor.forClass(College.class);
      verify(collegeRepository).save(collegeCaptor.capture());

      assertThat(collegeCaptor.getValue()).isNotNull();
      assertThat(collegeCaptor.getValue().getId()).isEqualTo(collegeId);
      assertThat(collegeCaptor.getValue().getCity()).isEqualTo(college.getCity());
      assertThat(collegeCaptor.getValue().getPostcode()).isEqualTo(college.getPostcode());
      // Updated fields
      assertThat(collegeCaptor.getValue().getAcronym()).isEqualTo(collegeDto.getAcronym());
      assertThat(collegeCaptor.getValue().getAddress()).isEqualTo(collegeDto.getAddress());
      assertThat(collegeCaptor.getValue().getWebsite()).isEqualTo(collegeDto.getWebsite());
      assertThat(collegeCaptor.getValue().getDescription()).isEqualTo(collegeDto.getDescription());

    }
  }

}


