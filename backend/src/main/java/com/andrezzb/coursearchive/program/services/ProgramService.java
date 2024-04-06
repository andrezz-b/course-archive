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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ProgramService {

  private final ProgramRepository programRepository;
  private final CollegeService collegeService;
  private final ModelMapper modelMapper;

  @Autowired
  private MutableAclService aclService;

  public ProgramService(ProgramRepository programRepository, CollegeService collegeService,
      ModelMapper modelMapper) {
    this.programRepository = programRepository;
    this.collegeService = collegeService;
    this.modelMapper = modelMapper;
  }

  public Page<Program> findAllProgramsPaged(Pageable p) {
    return programRepository.findAll(p);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.program.models.Program', admin)")
  public Program findProgramById(Long id) {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException(id));
  }

  @Transactional
  public Program createProgram(ProgramCreateDto programDto) {
    var college = collegeService.findCollegeById(programDto.getCollegeId());
    Program program = modelMapper.map(programDto, Program.class);
    program.setCollege(college);
    Program savedProgram = programRepository.save(program);
    /* ACL - Move to advice */
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    ObjectIdentity oi = new ObjectIdentityImpl(savedProgram);
    Sid sid = new PrincipalSid(username);
    Permission p = BasePermission.ADMINISTRATION;
    MutableAcl acl = null;
    try {
        acl = (MutableAcl) aclService.readAclById(oi);
    } catch (NotFoundException nfe) {
        acl = aclService.createAcl(oi);
    }

    // Now grant some permissions via an access control entry (ACE)
    acl.insertAce(acl.getEntries().size(), p, sid, true);
    // Set parent ACL
    acl.setEntriesInheriting(true);
    Acl parentAcl = aclService.readAclById(new ObjectIdentityImpl(college));
    acl.setParent(parentAcl);
    aclService.updateAcl(acl);
    /* ACL - Move to advice */
    return savedProgram;
  }

  public Program updateProgram(Long id, @Valid ProgramUpdateDto programUpdateDto) {
    Program program = findProgramById(id);
    modelMapper.map(programUpdateDto, program);
    return programRepository.save(program);
  }

  public void deleteProgramById(Long id) {
    programRepository.deleteById(id);
  }

}
