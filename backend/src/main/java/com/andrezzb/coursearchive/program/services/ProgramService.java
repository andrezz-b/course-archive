package com.andrezzb.coursearchive.program.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.dto.ProgramDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.exceptions.ProgramNotFoundException;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.repository.ProgramRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class ProgramService {

  private final ProgramRepository programRepository;
  private final CollegeService collegeService;
  private final ModelMapper modelMapper;
  private final AclUtilService aclUtilService;


  public ProgramService(ProgramRepository programRepository, CollegeService collegeService,
      ModelMapper modelMapper, AclUtilService aclUtilService) {
    this.programRepository = programRepository;
    this.collegeService = collegeService;
    this.modelMapper = modelMapper;
    this.aclUtilService = aclUtilService;
  }

  @PreAuthorize("hasRole('USER')")
  public Page<ProgramDto> findAllProgramsPaged(Pageable p, List<String> filterField,
      List<Object> filterValue, Long collegeId) {
    Page<Program> programs = programRepository.findAllByFilterFieldAndValue(p, filterField,
        filterValue, collegeId);
    return programs.map(program -> modelMapper.map(program, ProgramDto.class));

  }

  @PreAuthorize("hasRole('USER')")
  public ProgramDto findProgramById(Long id) {
    var program = findProgram(id);
    return modelMapper.map(program, ProgramDto.class);
  }

  public Program findProgram(Long id) {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#programDto.collegeId, 'com.andrezzb.coursearchive.college.models.College', 'create') || hasRole('MANAGER')")
  public ProgramDto createProgram(ProgramCreateDto programDto) {
    var college = collegeService.findCollege(programDto.getCollegeId());
    Program program = modelMapper.map(programDto, Program.class);
    program.setCollege(college);
    program.setId(null);
    Program savedProgram = programRepository.save(program);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedProgram, username, AclPermission.ADMINISTRATION);
    return modelMapper.map(savedProgram, ProgramDto.class);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.program.models.Program', 'write') || hasRole('MANAGER')")
  public ProgramDto updateProgram(Long id, @Valid ProgramUpdateDto programUpdateDto) {
    Program program = findProgram(id);
    modelMapper.map(programUpdateDto, program);
    var savedProgram = programRepository.save(program);
    return modelMapper.map(savedProgram, ProgramDto.class);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.program.models.Program', 'delete') || hasRole('MANAGER')")
  public void deleteProgramById(Long id) {
    programRepository.deleteById(id);
  }

}
