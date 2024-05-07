package com.andrezzb.coursearchive.program.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
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
  public Page<Program> findAllProgramsPaged(Pageable p) {
    return programRepository.findAll(p);
  }

  @PreAuthorize("hasRole('USER')")
  public Program findProgramById(Long id) {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#programDto.collegeId, 'com.andrezzb.coursearchive.college.models.College', create) || hasRole('MANAGER')")
  public Program createProgram(ProgramCreateDto programDto) {
    var college = collegeService.findCollege(programDto.getCollegeId());
    Program program = modelMapper.map(programDto, Program.class);
    program.setCollege(college);
    Program savedProgram = programRepository.save(program);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedProgram, username, AclPermission.ADMINISTRATION);
    return savedProgram;
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.program.models.Program', write) || hasRole('MANAGER')")
  public Program updateProgram(Long id, @Valid ProgramUpdateDto programUpdateDto) {
    Program program = findProgramById(id);
    modelMapper.map(programUpdateDto, program);
    return programRepository.save(program);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.program.models.Program', delete) || hasRole('MANAGER')")
  public void deleteProgramById(Long id) {
    programRepository.deleteById(id);
  }

}
