package com.andrezzb.coursearchive.program.services;

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
import com.andrezzb.coursearchive.college.exceptions.CollegeNotFoundException;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.config.GlobalConfig;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.exceptions.ProgramNotFoundException;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.repository.ProgramRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceUnitTest {
  @Spy
  private final ModelMapper modelMapper = new GlobalConfig().modelMapper();

  @Mock
  private ProgramRepository programRepository;

  @Mock
  private AclUtilService aclUtilService;

  @Mock
  private CollegeService collegeService;

  @InjectMocks
  private ProgramService programService;

  @Nested
  @DisplayName("findProgramById() Tests")
  class FindPorgramById {
    @Test
    void givenMissingId_whenFindById_thenThrowsException() {
      Long programId = 1L;

      when(programRepository.findById(programId)).thenReturn(Optional.empty());
      assertThatThrownBy(() -> programService.findProgramById(programId))
          .isInstanceOf(ProgramNotFoundException.class);
    }

    @Nested
    @DisplayName("createProgram() Tests")
    class CreateProgram {

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
      void givneInvalidCollegeId_whenCreateProgram_thenCollegeNotFoundIsThrown() {
        ProgramCreateDto createDto =
            ProgramCreateDto.builder().name("Test program").collegeId(1L).build();

        when(collegeService.findCollegeById(1L)).thenThrow(new CollegeNotFoundException(1L));

        assertThatThrownBy(() -> programService.createProgram(createDto))
            .isInstanceOf(CollegeNotFoundException.class);
      }

      @Test
      void givenValidProgramDto_whenCreateProgram_thenProgramCreated() {
        ProgramCreateDto createDto = ProgramCreateDto.builder().name("Test program").collegeId(1L)
            .duration((short) 2).degreeType("Cool").degreeTitle("Test title")
            .degreeTitleAbbreviation("mag").description("Description").build();

        College college = new College();
        college.setId(1L);

        when(collegeService.findCollegeById(college.getId())).thenReturn(college);
        when(programRepository.save(any(Program.class))).thenReturn(null);

        programService.createProgram(createDto);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        verify(collegeService).findCollegeById(1L);

        verify(aclUtilService).grantPermission(eq(null), any(String.class),
            eq(AclPermission.ADMINISTRATION));
        verify(programRepository).save(programCaptor.capture());
        Program program = programCaptor.getValue();

        assertThat(program).isNotNull();
        assertThat(program.getName()).isEqualTo(createDto.getName());
        assertThat(program.getCollege()).isEqualTo(college);
        assertThat(program.getDuration()).isEqualTo(createDto.getDuration());
        assertThat(program.getDegreeType()).isEqualTo(createDto.getDegreeType());
        assertThat(program.getDegreeTitle()).isEqualTo(createDto.getDegreeTitle());
        assertThat(program.getDegreeTitleAbbreviation())
            .isEqualTo(createDto.getDegreeTitleAbbreviation());
        assertThat(program.getDescription()).isEqualTo(createDto.getDescription());

      }
    }
  }


  @Nested
  @DisplayName("updateProgram() Tests")
  class UpdateProgram {
    @Test
    void givenMissingId_whenUpdateProgram_thenProgramNotFoundExceptionIsThrown() {
      // Arrange
      Long programId = 1L;
      ProgramUpdateDto updateDto =
          ProgramUpdateDto.builder().description("Updated description").build();

      when(programRepository.findById(programId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> programService.updateProgram(programId, updateDto))
          .isInstanceOf(ProgramNotFoundException.class);

      verify(programRepository).findById(programId);
    }

    @Test
    void givenDescriptionOnly_whenUpdateProgram_thenDescriptionIsUpdated() {
      // Arrange
      Long programId = 1L;
      String updatedDescription = "Updated description";
      ProgramUpdateDto updateDto =
          ProgramUpdateDto.builder().description(updatedDescription).build();

      Program existingProgram = new Program();
      existingProgram.setId(programId);
      existingProgram.setName("Program name");
      existingProgram.setDescription("Old description");

      when(programRepository.findById(programId)).thenReturn(Optional.of(existingProgram));
      when(programRepository.save(any(Program.class))).thenReturn(null);

      // Act
      programService.updateProgram(programId, updateDto);

      // Assert

      var programCaptor = ArgumentCaptor.forClass(Program.class);
      verify(programRepository).findById(programId);
      verify(programRepository).save(programCaptor.capture());

      Program programSaved = programCaptor.getValue();

      assertThat(programSaved).isNotNull();
      assertThat(programSaved.getName()).isEqualTo(existingProgram.getName());
      assertThat(programSaved.getDescription()).isEqualTo(updatedDescription);
    }

  }
}
